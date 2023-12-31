package com.example.weatherapp.feature_city_search.present.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CardCityBinding
import com.example.weatherapp.feature_city_search.data.data_source.model.CityEntity
import com.example.weatherapp.feature_city_search.domain.model.CityResult
import com.example.weatherapp.feature_city_search.present.adapter.model.CityAdapterModel
import com.example.weatherapp.util.NetworkResult

class CitiesAdapter(
    private val onCityListener: OnCityListener,
) : RecyclerView.Adapter<CitiesAdapter.CityHolder>() {
    private var cityEntities = listOf<CityEntity>()

    inner class CityHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = CardCityBinding.bind(view)

        fun bind(
            cityAdapterModel: CityAdapterModel
        ) = with(binding) {
            tvCityName.text = cityAdapterModel.cityName
            tvCityInfo.text = cityAdapterModel.cityInfo
            when (cityAdapterModel.cityEntity.inFavorite){
                true -> {
                    imFavorite.setImageResource(R.drawable.ic_star_filled)
                }
                false -> {
                    imFavorite.setImageResource(R.drawable.ic_star_outfilled)
                }
            }
            itemView.setOnClickListener {
                onCityListener.onCityClick(
                    NetworkResult.Success(
                        CityResult(
                            id = cityAdapterModel.cityEntity.id,
                            name = cityAdapterModel.cityEntity.name,
                            latitude = cityAdapterModel.cityEntity.latitude,
                            longitude = cityAdapterModel.cityEntity.longitude,
                            countryCode = cityAdapterModel.cityEntity.countryCode,
                            population = cityAdapterModel.cityEntity.population,
                            country = cityAdapterModel.cityEntity.country,
                            admin = cityAdapterModel.cityEntity.admin
                        )
                    )
                )
            }
            imFavorite.setOnClickListener {
                onCityListener.onFavoriteClick(cityAdapterModel.cityEntity)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CityHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            /* resource = */ R.layout.card_city,
            /* root = */ parent,
            /* attachToRoot = */ false
        )
        return CityHolder(view = view)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        cityEntities[position].apply {
            val cityName = this.name
            val between = when {
                this.admin.isBlank() || this.country.isBlank() -> ""
                else -> ", "
            }
            val cityInfo = this.admin + between + this.country
            holder.bind(
                cityAdapterModel = CityAdapterModel(
                    cityName = cityName,
                    cityInfo = cityInfo,
                    cityEntity = this,
                    onCityListener = onCityListener
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return cityEntities.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCityEntities(cityEntities: List<CityEntity>) {
        this.cityEntities = cityEntities
        notifyDataSetChanged()
    }

    interface OnCityListener {
        fun onCityClick(cityResult: NetworkResult<CityResult>)

        fun onFavoriteClick(cityEntity: CityEntity)
    }
}