package club.zarddy.ishadowx.adapter;

import club.zarddy.ishadowx.BR;
import club.zarddy.ishadowx.R;
import club.zarddy.ishadowx.databinding.AdapterIshadowAccountItemBinding;
import club.zarddy.ishadowx.model.IShadowAccount;
import club.zarddy.library.adapter.BaseDBRecyclerViewAdapter;

public class IShadowAccountListAdapter extends BaseDBRecyclerViewAdapter<IShadowAccount, AdapterIshadowAccountItemBinding> {

    public IShadowAccountListAdapter() {
        super(R.layout.adapter_ishadow_account_item, BR.account);
    }
}
