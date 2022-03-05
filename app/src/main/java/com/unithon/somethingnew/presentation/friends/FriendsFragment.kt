package com.unithon.somethingnew.presentation.friends

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.KEY_UID
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.MainApi
import com.unithon.somethingnew.data.network.response.FriendResponse
import com.unithon.somethingnew.databinding.FriendsFragmentBinding
import com.unithon.somethingnew.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.dialog_add_frend.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FriendsFragment(override val layoutResId: Int = R.layout.friends_fragment) :
    BaseFragment<FriendsFragmentBinding>(), CoroutineScope {

    private lateinit var addDialog: Dialog
    private lateinit var job: Job
    private val listArray = MutableLiveData<List<FriendResponse>>(null)
    private lateinit var itemAdapter: FriendRecyclerView
    private lateinit var preferenceManager: PreferenceManager
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        job = Job()

        binding.recyclerview.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL,
            false
        )

        addDialog = Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_add_frend)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            cancelBtn.setOnClickListener {
                this.dismiss()
            }
            okBtn.setOnClickListener {
                val email = email_edit_text.text.toString()
                val myUid = preferenceManager.getLong(KEY_UID)
                launch(Dispatchers.IO) {
                    if (email.isNullOrBlank().not()) {
                        val isSuccess: Boolean = MainApi().requestFriend(myUid, email)
                        if (isSuccess.not()) {
                            launch(Dispatchers.Main) {
                                is_vaild.visibility = View.VISIBLE
                            }
                        } else {
                            listArray.value = MainApi().getFriendList(myUid)
                            launch(Dispatchers.Main) {
                                dismiss()
                            }
                        }
                    }
                }
            }
        }

        itemAdapter = FriendRecyclerView(listArray.value?.toMutableList() ?: mutableListOf(), requireContext())
        binding.recyclerview.adapter = itemAdapter

        preferenceManager = PreferenceManager(requireContext())

        launch(Dispatchers.IO) {
            listArray.postValue(MainApi().getFriendList(preferenceManager.getLong(KEY_UID)))
        }

        listArray.observe(this) {
            if (it != null) {
                itemAdapter.updateItems(it)
            }
        }

        binding.addFriendBtn.setOnClickListener {
            addDialog.show()
        }

    }

}