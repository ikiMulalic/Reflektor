package ikbal.mulalic.reflektor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InstructionsActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button mPrevBtn;
    private Button mNextBtn;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        mPrevBtn = (Button) findViewById(R.id.prevBtn);
        mNextBtn = (Button) findViewById(R.id.nextBtn);

        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        //OnClickListeners

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNextBtn.getText().equals("Završi")) {
                    Intent intent = new Intent(InstructionsActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    mSlideViewPager.setCurrentItem(mCurrentPage + 1);
                }
            }
        });

        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[5];
        mDotLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(50);
            mDots[i].setTextColor(getResources().getColor(R.color.backgroundColor));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;

            switch (position)
            {
                case 0:
                    mNextBtn.setEnabled(true);
                    mPrevBtn.setEnabled(false);
                    mPrevBtn.setVisibility(View.INVISIBLE);

                    mPrevBtn.setText("");
                    mNextBtn.setText("Dalje");
                    break;
                case 1:
                    mNextBtn.setEnabled(true);
                    mPrevBtn.setEnabled(true);
                    mPrevBtn.setVisibility(View.VISIBLE);

                    mPrevBtn.setText("Nazad");
                    mNextBtn.setText("Dalje");
                    break;
                case 2:
                    mNextBtn.setEnabled(true);
                    mPrevBtn.setEnabled(true);
                    mPrevBtn.setVisibility(View.VISIBLE);

                    mPrevBtn.setText("Nazad");
                    mNextBtn.setText("Dalje");
                    break;
                case 3:
                    mNextBtn.setEnabled(true);
                    mPrevBtn.setEnabled(true);
                    mPrevBtn.setVisibility(View.VISIBLE);

                    mPrevBtn.setText("Nazad");
                    mNextBtn.setText("Dalje");
                    break;
                case 4:
                    mNextBtn.setEnabled(true);
                    mPrevBtn.setEnabled(true);
                    mPrevBtn.setVisibility(View.VISIBLE);

                    mPrevBtn.setText("Nazad");
                    mNextBtn.setText("Završi");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}