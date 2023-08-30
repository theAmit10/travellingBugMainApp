package com.travel.travellingbug.ui.activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.travel.travellingbug.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfPogoActivity extends AppCompatActivity {

    Button pdfBtn;
    Uri pdfUriParent;

    private static final String FILE_PROVIDER_AUTHORITY = "com.travel.travellingbug.provider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_pogo);

        pdfBtn = findViewById(R.id.pdfBtn);


        pdfBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                Toast.makeText(PdfPogoActivity.this, "GENERATING", Toast.LENGTH_SHORT).show();
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

                generateAndSavePdf();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void savePdfAndLogUri() {
        try {
//            InputStream in = getAssets().open("eliza.pdf");
            InputStream in = getAssets().open("my_pdf.pdf");
//            Uri savedFileUri = savePDFFile(PdfPogoActivity.this, in, "files/pdf", "my_pdf.pdf", "resume");
            Uri savedFileUri = savePDFFile(PdfPogoActivity.this, in, "files/pdf", "my_pdf.pdf", "document");
//            Uri savedFileUri = savePDFFile(PdfPogoActivity.this, in, "/pdf", "eliza.pdf", "resume");
            Log.d("URI: ", savedFileUri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    private Uri savePDFFile(@NonNull final Context context, @NonNull InputStream in,
                            @NonNull final String mimeType,
                            @NonNull final String displayName, @Nullable final String subFolder) throws IOException {
        String relativeLocation = Environment.DIRECTORY_DOCUMENTS;

        if (!TextUtils.isEmpty(subFolder)) {
            relativeLocation += File.separator + subFolder;
        }

        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);
        contentValues.put(MediaStore.Video.Media.TITLE, "SomeName");
        contentValues.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        contentValues.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
        final ContentResolver resolver = context.getContentResolver();
        OutputStream stream = null;
        Uri uri = null;

        try {
            final Uri contentUri = MediaStore.Files.getContentUri("external");
            uri = resolver.insert(contentUri, contentValues);
            ParcelFileDescriptor pfd;
            try {
                assert uri != null;
                pfd = getContentResolver().openFileDescriptor(uri, "w");
                assert pfd != null;
                FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());


                byte[] buf = new byte[4 * 1024];
                int len;
                while ((len = in.read(buf)) > 0) {

                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
                pfd.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            contentValues.clear();
            contentValues.put(MediaStore.Video.Media.IS_PENDING, 0);
            getContentResolver().update(uri, contentValues, null, null);
            stream = resolver.openOutputStream(uri);
            if (stream == null) {
                throw new IOException("Failed to get output stream.");
            }
            return uri;
        } catch (IOException e) {
            // Don't leave an orphan entry in the MediaStore
            resolver.delete(uri, null, null);
            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void generateAndSavePdf() {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 500, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText("Hello, PDF!", 80, 50, paint);

        pdfDocument.finishPage(page);

        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "my_pdf.pdf");
        try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
            pdfDocument.writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pdfDocument.close();
        }

        // Launch PDF viewer
        Uri pdfUri = FileProvider.getUriForFile(this, "com.travel.travellingbug.provider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void generateAndSavePdfO() {


        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 500, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText("Hello, PDF!", 80, 50, paint);

        pdfDocument.finishPage(page);

        File pdfFile = new File(getExternalFilesDir(null), "my_pdf.pdf");
        try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
            pdfDocument.writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pdfDocument.close();
        }

        Uri pdfUri = FileProvider.getUriForFile(this, "com.travel.travellingbug.provider", pdfFile);
        pdfUriParent = pdfUri;
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(pdfUri, "application/pdf");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivity(intent);


        // Use SAF to save the PDF

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("files/pdf");
//        intent.setType("application/pdf");
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "my_pdf.pdf");
        pdfUriParent = pdfUri;
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pdfUri);
        startActivityForResult(intent, 0);


//        PdfDocument pdfDocument = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 500, 1).create();
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//
//        Canvas canvas = page.getCanvas();
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        canvas.drawText("Hello, PDF!", 80, 50, paint);
//
//        pdfDocument.finishPage(page);
//
//        // Get the directory where you want to save the PDF
//
//        File pdfDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "travellingbug");
//        pdfDir.mkdirs();
//
//        File pdfFile = new File(pdfDir, "my_pdf.pdf");
//        try(FileOutputStream outputStream = new FileOutputStream(pdfFile))  {
//
//            pdfDocument.writeTo(outputStream);
////            pdfDocument.writeTo(outputStream);
//            pdfDocument.close();
////            System.out.println("outstream data : "+outputStream.toString());
////            System.out.println("outstream data : "+pdfDocument.toString());
//
//            // Use SAF to save the PDF
//            Uri pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", pdfFile);
//            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
////            intent.setType("files/pdf");
//            intent.setType("application/pdf");
//            intent.putExtra(Intent.EXTRA_TITLE, "my_pdf.pdf");
//            pdfUriParent = pdfUri;
//            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pdfUri);
//            startActivityForResult(intent, 0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        PdfDocument pdfDocument = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 500, 1).create();
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//
//        Canvas canvas = page.getCanvas();
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        canvas.drawText("Hello, PDF!", 80, 50, paint);
//
//        pdfDocument.finishPage(page);
//
//        File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "my_pdf.pdf");
//        try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
//            pdfDocument.writeTo(outputStream);
//            pdfDocument.close();
//            Toast.makeText(this, "PDF saved successfully", Toast.LENGTH_SHORT).show();
//
//            // Open PDF using default viewer
//            Uri pdfUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, pdfFile);
//            Intent viewPdfIntent = new Intent(Intent.ACTION_VIEW);
//            viewPdfIntent.setDataAndType(pdfUri, "application/pdf");
//            viewPdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            startActivity(viewPdfIntent);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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


    @RequiresApi(api = Build.VERSION_CODES.Q)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 12)
//        {
//            savePdfAndLogUri();
//
//        }
//
//        if (resultCode == RESULT_OK) {
//            if (data != null && data.getData() != null) {
//                // PDF has been saved using SAF
////                savePdfAndLogUri();
//                System.out.println("Data uri after creating pdf : "+data.getData());
//                System.out.println("Data uri after creating pdf pdfUriParent  : "+pdfUriParent);
//
//                savePdfAndLogUri();
//                Toast.makeText(this, "Saved Success", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {

                System.out.println("Data uri after creating pdf : "+data.getData());
                System.out.println("Data uri after creating pdf pdfUriParent  : "+pdfUriParent);
                // Get the URI of the saved PDF
                Uri savedPdfUri = data.getData();

                // Launch an intent to view the PDF
                Intent viewPdfIntent = new Intent(Intent.ACTION_VIEW);
                viewPdfIntent.setDataAndType(savedPdfUri, "application/pdf");
                viewPdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(viewPdfIntent);
                } catch (Exception e) {
                    // Handle exception if no PDF viewer app is available
                    e.printStackTrace();
                }

                Toast.makeText(this, "Saved Success", Toast.LENGTH_SHORT).show();

            }
        }}
}