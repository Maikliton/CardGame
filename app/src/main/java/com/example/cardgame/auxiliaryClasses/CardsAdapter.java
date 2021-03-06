package com.example.cardgame.auxiliaryClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cardgame.GameActivity;
import com.example.cardgame.R;
import com.example.cardgame.Views.Card_View_Final;
import com.example.cardgame.auxiliaryClasses.forRetrofit.MyServer;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseRequest;
import com.example.cardgame.auxiliaryClasses.forRetrofit.UniverseResponse;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.cardgame.GameActivity.API_URL;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    private ArrayList<Card> cards;
    private Context ctx;
    private int token;
    private Retrofit retrofit;
    private MyServer server;

    public CardsAdapter(ArrayList<Card> cards, int token, Context ctx) {
        this.cards = cards;
        this.ctx = ctx;
        this.token = token;
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        server = retrofit.create(MyServer.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(ctx).
                inflate(R.layout.card_item_list, parent, false);
        convertView.setOnClickListener(v -> {
            if(!WaitForMove.isEnabled) return;
            if(((Card_View_Final) v.findViewById(R.id.card)).nowCard.equals(GameActivity.table.get(0))) {
                GameActivity.waitForMove.show();
                @SuppressLint("CutPasteId") Call<UniverseResponse> call = server.throwCard(UniverseRequest.ThrowCard(token, ((Card_View_Final) v.findViewById(R.id.card)).nowCard));
                call.enqueue(new Callback<UniverseResponse>() {
                @Override
                public void onResponse(Call<UniverseResponse> call, Response<UniverseResponse> response) {
                        @SuppressLint("CutPasteId") int index = GameActivity.cards.indexOf(((Card_View_Final) v.findViewById(R.id.card)).nowCard);
                        if(index == -1) {
                            GameActivity.adapter.notifyDataSetChanged();
                        } else {
                            GameActivity.cards.remove(index);
                            GameActivity.adapter.notifyItemRemoved(index);
                        }
                        if(response.body() != null) {
                            GameActivity.score.setText(String.valueOf(response.body().score));
                        }
                }

                @Override
                public void onFailure(Call<UniverseResponse> call, Throwable t) {
                    Log.d("DEBUG", Objects.requireNonNull(t.getMessage()));
                    GameActivity.waitForMove.hide();
                }
            });
        } else {
                Toast.makeText(ctx, "Вы не можете кинуть эту карту", Toast.LENGTH_SHORT).show();
                GameActivity.waitForMove.hide();
            }
        });
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cards.get(position);
        ((Card_View_Final) holder.itemView.findViewById(R.id.card)).changeCard(card);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
