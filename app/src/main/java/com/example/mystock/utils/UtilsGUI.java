package com.example.mystock.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.mystock.R;

public class UtilsGUI {

    public static void aviso(Context context, int idTexto){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.aviso);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(idTexto);

        builder.setNeutralButton(R.string.ok, null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void confirmaçao(Context context,
                                   String mensagem,
                                   DialogInterface.OnClickListener listener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.confirmacao);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setMessage(mensagem);

        builder.setPositiveButton(R.string.sim, listener);
        builder.setNegativeButton(R.string.nao, listener);

        AlertDialog alert = builder.create();
        alert.show();
    }

}
