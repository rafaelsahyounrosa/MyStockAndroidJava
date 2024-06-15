package com.example.mystock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mystock.model.Produto;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProdutoAdapter extends BaseAdapter {

    private Context context;
    private List<Produto> produtos;
    private NumberFormat numberFormat;

    private static class ProdutoHolder{
        public TextView textViewValorNome;
        public TextView textViewValorQuantidade;
        public TextView textViewValorImportante;
        public TextView textViewValorCriticidade;
        public TextView textViewValorCategoria;
    }

    public ProdutoAdapter(Context context, List<Produto> produtos) {
        this.context = context;
        this.produtos = produtos;
        numberFormat = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Object getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ProdutoHolder holder;

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_lista_produto, viewGroup, false);

            holder = new ProdutoHolder();
            holder.textViewValorNome = view.findViewById(R.id.textViewValorNome);
            holder.textViewValorCategoria = view.findViewById(R.id.textViewValorCategoria);
            holder.textViewValorQuantidade = view.findViewById(R.id.textViewValorQuantidade);
            holder.textViewValorImportante = view.findViewById(R.id.textViewValorImportante);
            holder.textViewValorCriticidade = view.findViewById(R.id.textViewValorCriticidade);

            view.setTag(holder);

        } else {
            holder = (ProdutoHolder) view.getTag();
        }

        holder.textViewValorNome.setText(produtos.get(i).getNome());

/*
 TODO perguntar se há alguma vantagem em usar o number format quando formos usar a internacionalização
 visto que são números puros e não moeda ou unidades de medida.
        String quantidadeFormatada = numberFormat.format(produtos.get(i).getQuantidade());
        holder.textViewValorQuantidade.setText(quantidadeFormatada);
*/
        holder.textViewValorQuantidade.setText(String.valueOf(produtos.get(i).getQuantidade()));



        if(produtos.get(i).isImportante()){
            holder.textViewValorImportante.setText(context.getResources().getString(R.string.sim));
        }else {
            holder.textViewValorImportante.setText(context.getResources().getString(R.string.nao));
        }
        switch (produtos.get(i).getCategoria()){
            case Comida:
                holder.textViewValorCategoria.setText(R.string.comida);
                break;
            case Limpeza:
                holder.textViewValorCategoria.setText(R.string.limpeza);
                break;
            case Higiene:
                holder.textViewValorCategoria.setText(R.string.higiene);
                break;
        }
        switch (produtos.get(i).getCriticidade()){
            case Baixa:
                holder.textViewValorCriticidade.setText(R.string.baixa);
                break;
            case Media:
                holder.textViewValorCriticidade.setText(R.string.media);
                break;
            case Alta:
                holder.textViewValorCriticidade.setText(R.string.alta);
                break;
            default:
                holder.textViewValorCriticidade.setText(R.string.semCriticidade);
        }


        return view;
    }
}
