package silva.fellipy.galeriapublica;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import kotlinx.coroutines.CoroutineScope;

// Classe que estende AndroidViewModel para gerenciar dados relacionados à IU.
public class MainViewModel extends AndroidViewModel {
    // Variável que armazena a opção de navegação selecionada.
    int navigationOpSelected = R.id.gridViewOp;

    // LiveData que conterá os dados paginados das imagens.
    LiveData<PagingData<ImageData>> pageLv;

    // Construtor da classe.
    public MainViewModel(@NonNull Application application) {
        super(application);

        // Cria uma instância do repositório para manipular os dados da galeria.
        GalleryRepository galleryRepository = new GalleryRepository(application);

        // Cria uma instância da fonte de dados paginados da galeria.
        GalleryPagingSource galleryPagingSource = new GalleryPagingSource(galleryRepository);

        // Configuração da paginação, definindo o tamanho da página como 10.
        Pager<Integer, ImageData> pager = new Pager(new PagingConfig(10), () -> galleryPagingSource);

        // Obtém o escopo de CoroutineScope associado a este ViewModel.
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        // Cria uma LiveData paginada que contém os dados paginados da galeria.
        pageLv = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    // Método para obter a LiveData contendo os dados paginados das imagens.
    public LiveData<PagingData<ImageData>> getPageLv() {
        return pageLv;
    }

    // Método para obter a opção de navegação selecionada.
    public int getNavigationOpSelected() {
        return navigationOpSelected;
    }

    // Método para definir a opção de navegação selecionada.
    public void setNavigationOpSelected(int navigationOpSelected) {
        this.navigationOpSelected = navigationOpSelected;
    }
}
