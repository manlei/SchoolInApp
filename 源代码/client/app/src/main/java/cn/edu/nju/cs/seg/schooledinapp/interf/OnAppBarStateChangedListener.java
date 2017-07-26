package cn.edu.nju.cs.seg.schooledinapp.interf;

import android.support.design.widget.AppBarLayout;

/**
 * AppBarLayout 状态监听器
 *
 */
public abstract class OnAppBarStateChangedListener implements AppBarLayout.OnOffsetChangedListener {

    // 状态
    public enum State {
        EXPANDED,
        COLLAPSED,
        CLOSE_COLLAPSEC,
        CLOSE_EXPANDED
    }

    // 因子
    public static double collapseParallaxMultiplier = 0.7;

    private State currentState = State.CLOSE_EXPANDED;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (currentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED, verticalOffset);
            }
            currentState = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (currentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED, verticalOffset);
            }
            currentState = State.COLLAPSED;
        } else if (Math.abs(verticalOffset) >= collapseParallaxMultiplier * appBarLayout.getTotalScrollRange()) {
            if (currentState != State.CLOSE_COLLAPSEC) {
                onStateChanged(appBarLayout, State.CLOSE_COLLAPSEC, verticalOffset);
            }
            currentState = State.CLOSE_COLLAPSEC;
        } else {
            if (currentState != State.CLOSE_EXPANDED) {
                onStateChanged(appBarLayout, State.CLOSE_EXPANDED, verticalOffset);
            }
            currentState = State.CLOSE_EXPANDED;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset);
}
