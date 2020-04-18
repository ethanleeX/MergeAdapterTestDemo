package masteryi.me.mergeadapterdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView
import masteryi.me.mergeadapterdemo.databinding.ActivityMainBinding

/**
 * @author Ethan Lee
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var headerAdapter: HeaderAdapter
    private lateinit var footerAdapter: FooterAdapter
    private lateinit var mergeAdapter: MergeAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.data.observe(this, Observer {
            itemAdapter.addItem(it)
        })
        viewModel.hasMoreState.observe(this, Observer {
            footerAdapter.updateFooterState(it)
        })

        headerAdapter = HeaderAdapter(this)
        itemAdapter = ItemAdapter(this)
        footerAdapter = FooterAdapter(this)
        mergeAdapter = MergeAdapter(headerAdapter, itemAdapter, footerAdapter)

        val layoutManager = LinearLayoutManager(this)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = mergeAdapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && layoutManager.findLastVisibleItemPosition() == mergeAdapter.itemCount - 1
                ) {
                    viewModel.loadMore()
                }
            }
        })
    }
}
