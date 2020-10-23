package ikbal.mulalic.reflektor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context)
    {
        this.context = context;
    }


    public String[] slide_headings = {"Podnošenje prijave","Postupanje sa prijavama","Zaštita podataka","O aplikaciji","Zahvaljujemo što koristite Reflektor i pomažete da rasvijetlimo izborne nepravilnosti!"};

    public String[] slide_descs = {

            "Novu prijavu korisnik podnosi na sljedeći način:\n" +
                    "1. Dodirom na fotoaparat ili videokameru, kako bi korisnik kreirao novu fotografiju ili video zapis\n" +
                    "2. Izbor kategorija – klikom na svaku od kategorija korisnik saznaje šta ista podrazumijeva\n" +
                    "3. Opis prijave – detaljan opis nepravilnosti koju korisnik prijavljuje, koji obavezno sadrži i naziv funkcionera/stranke/institucije na koju se prijava odnosi\n" +
                    "4. Lokacija – mjesto u kojem se desila nepravilnost koju korisnik prijavljuje \n" +
                    "5. Pošalji – potvrda o slanju",


            "Nakon zaprimanja prijava, TI BIH će proslijediti dio prijava koji su zakonski regulisane " +
                    "nadležnim institucijama na dalje postupanje. Imajući u vidu da u Bosni i Hercegovini još uvijek " +
                    " nije zakonski regulisana zloupotreba javnih resursa u svrhu predizborne promocije, " +
                    " dio prikupljenih prijava služiće kao osnov za dalje zagovaranje za izmjenu " +
                    "i ukazivanje na neophodnost regulisanja zakonskog okvira. ",

                    "Korisnici aplikacije koriste aplikaciju bez registracije i ne prosljeđuju svoje lične podatke kroz aplikaciju." +
                    "Sve prijave proslijeđene putem aplikacije su potpuno anonimne.",

            "Udruženje za borbu protiv korupcije „Transprency International“ u BiH (TI BIH) "  +
                    "razvilo je mobilnu aplikaciju kroz projekat “Podrška građanima u borbi protiv korupcije,”" +
                    " koji je finansijski podržan od strane Američke agencije za međunarodni razvoj (USAID)," +
                    " a koji implementira zajedno sa partnerskim organizacijama Centri civilnih inicijativa" +
                    "i Centrom za razvoj medija i analize." +
                    "Cilj aplikacije je izvještavanje građana u realnom vremenu o evenutalnim zloupotrebama javnih sredstava" +
                    "i javnih funkcija u svrhu izborne kampanje i nepravilnostima koje se pojavljuju u toku izborne kampanje.",

            ""
    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==(ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(R.mipmap.reflektor_logo_icon);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
