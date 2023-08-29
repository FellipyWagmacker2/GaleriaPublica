package silva.fellipy.galeriapublica;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;


public class ImageDataComparator extends DiffUtil.ItemCallback<ImageData> {

    @Override
    public boolean areItemsTheSame(@NonNull ImageData oldItem, @NonNull ImageData newItem) {
        // O ID é único, então comparamos os URIs para verificar a igualdade dos itens
        return oldItem.uri.equals(newItem.uri);
    }

    // Método para verificar se os conteúdos dos itens antigo e novo são os mesmos
    @Override
    public boolean areContentsTheSame(@NonNull ImageData oldItem, @NonNull ImageData newItem) {
        // Comparamos os URIs para verificar a igualdade dos conteúdos dos itens
        return oldItem.uri.equals(newItem.uri);
    }
}
