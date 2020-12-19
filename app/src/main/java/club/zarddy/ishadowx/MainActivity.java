package club.zarddy.ishadowx;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.List;

import club.zarddy.ishadowx.adapter.IShadowAccountListAdapter;
import club.zarddy.ishadowx.databinding.ActivityMainBinding;
import club.zarddy.ishadowx.model.IShadowAccount;
import club.zarddy.ishadowx.viewmodel.MainViewModel;
import club.zarddy.library.adapter.OnItemClickListener;
import club.zarddy.library.base.BaseActivity;
import club.zarddy.library.util.DeviceUtils;

public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> {

    private IShadowAccountListAdapter mListAdapter;

    @Override
    protected MainViewModel initViewModel() {
        return ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    protected int getContextViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mListAdapter = new IShadowAccountListAdapter();
        this.mDataBinding.layoutContentMain.recyclerView.setLayoutManager(layoutManager);
        this.mDataBinding.layoutContentMain.recyclerView.setAdapter(mListAdapter);
    }

    @Override
    protected void setData() {
        // 加载本地缓存数据
        mViewModel.loadAccountListCache();

        // 列表数据
        mViewModel.getIShadowAccountList().observe(this, new Observer<List<IShadowAccount>>() {
            @Override
            public void onChanged(List<IShadowAccount> accounts) {
                mListAdapter.setNewData(accounts);
            }
        });
    }

    @Override
    protected void setEvent() {
        mListAdapter.setOnItemClickListener(new OnItemClickListener<IShadowAccount>() {
            @Override
            public void onItemClick(View view, IShadowAccount iShadowAccount, int position) {
            }

            @Override
            public boolean onItemLongClick(View view, IShadowAccount iShadowAccount, int position) {
                // 长按，复制账号信息
                DeviceUtils.copyToClipboard(MainActivity.this, "iShadowAccount", iShadowAccount.toString());
                showMessage("已复制到剪贴板");
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                mViewModel.fetchAccountList();
                break;

            default:
                super.onClick(view);
                break;
        }
    }
}
