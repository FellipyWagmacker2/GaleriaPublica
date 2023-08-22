package silva.fellipy.galeriapublica;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Date;

public class ImageData {
    public Uri uri; //endereço uri do arquivo de foto
    public Bitmap thumb; // imagem em minitura da foto
    public String fileName; // nome do arquivo de foto
    public Date date; // data em que a foto foi criada
    public int size; // tamanho em bytes do arquivo de foto

    public ImageData(Uri uri, Bitmap thumb, String fileName, Date date, int size){
        this.uri = uri;
        this.thumb = thumb;
        this.fileName = fileName;
        this.date = date;
        this.size = size;
    }
}
