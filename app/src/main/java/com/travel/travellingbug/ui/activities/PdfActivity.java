package com.travel.travellingbug.ui.activities;

import static android.media.MediaRecorder.VideoSource.CAMERA;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import com.travel.travellingbug.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class PdfActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    private static final int CREATE_FILE_REQUEST_CODE = 2;
    private static final int CREATE_FILE_REQUEST_CODE_OT = 3;

    private ActivityResultLauncher<Intent> launcher; // Initialise this object in Activity.onCreate()
    private Uri baseDocumentTreeUri;
    private Uri imageUrl;



    Button pdfBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        launcher = new ActivityResultLauncher<Intent>() {
            @Override
            public void launch(Intent input, @Nullable ActivityOptionsCompat options) {

            }

            @Override
            public void unregister() {

            }

            @NonNull
            @Override
            public ActivityResultContract<Intent, ?> getContract() {
                return null;
            }
        };

        pdfBtn = findViewById(R.id.pdfBtn);








//        pdfBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(PdfActivity.this, "GENERATING", Toast.LENGTH_SHORT).show();
////                checkAndGeneratePdf();
//
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Uri imagePath = createPdf();
//                intent.putExtra(MediaStore.EXTRA_OUTPUT , imagePath);
//                startActivityForResult(intent,CAMERA);
//
//            }
//        });

        pdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PdfActivity.this, "GENERATING", Toast.LENGTH_SHORT).show();
//                checkAndGeneratePdf();

//                Intent intent = new Intent();
//                Uri imagePath = createPdf();
//                intent.putExtra(MediaStore.EXTRA_OUTPUT , imagePath);
//                startActivityForResult(intent,CAMERA);

//                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("application/pdf");
//                intent.putExtra(Intent.EXTRA_TITLE, "my_pdf.pdf");
//                startActivityForResult(intent,12);

                Intent intent = new Intent(PdfActivity.this, PdfPogoActivity.class);
                startActivity(intent);


            }
        });

    }

    private Uri createPdf(){
        Uri uri = null;
        ContentResolver resolver = getContentResolver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

        }else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        String imageName = "pogo";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imageName+".jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/"+"My image/");

        Uri finalUri = resolver.insert(uri, contentValues);

        imageUrl = finalUri;

        return finalUri;


    }

    public void writeFile(String fileName, String content)  {
        try {
            DocumentFile directory = DocumentFile.fromTreeUri(getApplicationContext(), baseDocumentTreeUri);
            DocumentFile file = directory.createFile("text/*", fileName);
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(file.getUri(), "w");
            FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());
            fos.write(content.getBytes());
            fos.close();
        } catch (IOException e) {

        }
    }

    private String readFile(String fileName) throws IOException {
        ContentResolver resolver = getContentResolver();
        Uri fileUri = baseDocumentTreeUri.buildUpon().appendPath(fileName).build();
        try (InputStream stream = resolver.openInputStream(fileUri)) {
            // Perform operations on "stream".
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            stream.close();
            return builder.toString();
        }
    }

    public void launchBaseDirectoryPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        launcher.launch(intent);
    }


    public void writeIntoFile(Context context, String fileName, String content) {
//        File appSpecificExternalStorageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File appSpecificInternalStorageDirectory = context.getFilesDir();
        File file = new File(appSpecificInternalStorageDirectory, fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            fos.write(content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public String readFromFile(String filePath) throws IOException {
        //        File appSpecificExternalStorageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File appSpecificInternalStorageDirectory = getApplicationContext().getFilesDir();
        File file = new File(appSpecificInternalStorageDirectory, filePath);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        fis.close();
        return builder.toString();
    }

    private void checkAndGeneratePdf() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("if checkAndGeneratePdf");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            System.out.println("else checkAndGeneratePdf");
            generateAndSavePdf();
        }
    }

    private void generateAndSavePdf() {
        System.out.println("generateAndSavePdf startd");
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 500, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText("Hello, PDF!", 80, 50, paint);

        pdfDocument.finishPage(page);

        File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "my_pdf.pdf");
        try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
            System.out.println("generateAndSavePdf pdf create");
            pdfDocument.writeTo(outputStream);


            Toast.makeText(this, "PDF generated and saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            System.out.println("generateAndSavePdf error");
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generateAndSavePdf();
            } else {
                Toast.makeText(this, "Permission denied. Cannot generate PDF.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handling result of file creation intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Handle the saved document URI here
                System.out.println("Activity resukt");

                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_TITLE, "my_pdf.pdf");
                startActivityForResult(intent,12);
            }


        }

        if (requestCode == CREATE_FILE_REQUEST_CODE_OT && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Handle the saved document URI here
                System.out.println("Activity resukt");

                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_TITLE, "my_pdf.pdf");
                startActivityForResult(intent,12);
            }

            if (data != null && data.getData() != null) {
                baseDocumentTreeUri = Objects.requireNonNull(data).getData();
                final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                // take persistable Uri Permission for future use
                getContentResolver().takePersistableUriPermission(data.getData(), takeFlags);
                SharedPreferences preferences = getSharedPreferences("com.travel.travellingbug.fileutility", Context.MODE_PRIVATE);
                preferences.edit().putString("filestorageuri", data.toString()).apply();
            }


        }

        if(requestCode == CAMERA){
            Toast.makeText(this, "Image Added", Toast.LENGTH_SHORT).show();

        }
    }
}