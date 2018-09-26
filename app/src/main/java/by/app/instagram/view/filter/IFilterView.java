package by.app.instagram.view.filter;

import android.view.View;

public class IFilterView {

   public interface Click{
       void clickApply();
    }

   interface View {
       void init();
       void initSpinnerDefault();
       void initClickListener();
       void initEtListener();
       void initTvCurrentDate();
       void showFilter();
       void hideFilter();
       void animClick();
       void updateFilter();
       void updateContentFilter();
       void showSelectDate();
       void hideSelectDate();
       void showInputCount();
       void hideInpunCount();
       void setTypeFilter(TypeFilter typeFilter);
       void setTypeSpinnerFilter(TypeSpinnerFilter typeSpinnerFilter);
       TypeSpinnerFilter getTypeSpinnerFilter();
       int getCountPosts();
       long getPeriod1();
       long getPeriod2();
   }

}
