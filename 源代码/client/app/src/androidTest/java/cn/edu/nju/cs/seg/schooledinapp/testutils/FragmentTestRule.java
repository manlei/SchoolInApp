package cn.edu.nju.cs.seg.schooledinapp.testutils;


import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import junit.framework.Assert;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.TestActivity;

/**
 * Reference:
 *  https://stackoverflow.com/questions/33647135/android-independent-fragment-ui-testing-tool/38393087#38393087
 * @param <F> 要测试的片段
 */
public class FragmentTestRule<F extends Fragment>
        extends ActivityTestRule<TestActivity> {

    private final Class<F> fragmentClass;

    private F fragment;

    public FragmentTestRule(final Class<F> fragmentClass) {
        super(TestActivity.class, true, false);
        this.fragmentClass = fragmentClass;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Instantiate and insert the fragment into the container layout
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    fragment = fragmentClass.newInstance();
                    transaction.replace(R.id.fl_test_container, fragment);
                    transaction.commit();
                } catch (InstantiationException | IllegalAccessException e) {
                    Assert.fail(String.format("%s: Could not insert %s into TestActivity: %s",
                            getClass().getSimpleName(),
                            fragmentClass.getSimpleName(),
                            e.getMessage()));
                }
            }
        });
    }

    public F getFragment(){
        return fragment;
    }

}