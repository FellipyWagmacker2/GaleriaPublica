package silva.fellipy.galeriapublica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;



public class GalleryPagingSource extends ListenableFuturePagingSource<Integer, ImageData> {
    GalleryRepository galleryRepository;  // Instância de GalleryRepository para carregar dados da galeria
    Integer initialLoadSize = 0;  // Tamanho de carregamento inicial dos dados


    public GalleryPagingSource(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, ImageData> pagingState) {
        return null;  // Chave de atualização nula, não há suporte para atualizações específicas
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<Integer, ImageData>> loadFuture(@NonNull LoadParams<Integer> loadParams) {
        Integer nextPageNumber = loadParams.getKey();  // Número da próxima página a ser carregada
        if (nextPageNumber == null) {
            nextPageNumber = 1;  // Se a próxima página for nula, é a primeira página
            initialLoadSize = loadParams.getLoadSize();  // Define o tamanho de carregamento inicial
        }

        Integer offSet = 0;  // Deslocamento inicial
        if(nextPageNumber == 2) {
            offSet = initialLoadSize;  // Se a próxima página for a segunda, deslocamento é o tamanho de carregamento inicial
        } else {

            offSet = ((nextPageNumber - 1) * loadParams.getLoadSize()) + (initialLoadSize - loadParams.getLoadSize()); // Caso contrário, calcula o deslocamento com base no número da página e tamanho de carregamento
        }

        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
        Integer finalOffSet = offSet;  // Deslocamento final para uso na thread
        Integer finalNextPageNumber = nextPageNumber;  // Número da próxima página para uso na thread


        ListenableFuture<LoadResult<Integer, ImageData>> lf = service.submit(new Callable<LoadResult<Integer, ImageData>>() { // Manda uma tarefa para carregar dados em uma thread
            @Override
            public LoadResult<Integer, ImageData> call() {
                List<ImageData> imageDataList = null;
                try {

                    imageDataList = galleryRepository.loadImageData(loadParams.getLoadSize(), finalOffSet); // Carrega os dados da galeria usando o repositório

                    Integer nextKey = null;
                    if(imageDataList.size() >= loadParams.getLoadSize()) {
                        nextKey = finalNextPageNumber + 1;  // Calcula a próxima chave se houver mais dados a serem carregados
                    }

                    return new LoadResult.Page<Integer, ImageData>(imageDataList, null, nextKey);  // Retorna os dados carregados e a chave da próxima página

                } catch (FileNotFoundException e) {
                    return new LoadResult.Error<>(e); // Retorna um erro se houver uma exceção de arquivo não encontrado
                }
            }
        });

        return lf;  // Retorna o futuro que representa o resultado do carregamento
    }
}
