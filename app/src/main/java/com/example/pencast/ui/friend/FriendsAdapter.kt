package com.example.pencast.ui.friend
//
//Will be used at later point
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.navigation.findNavController
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.pencast.R
//
//class FriendsAdapter() : ListAdapter<Friend, FriendsAdapter.FriendViewHolder>(FriendDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
//        val itemView =
//            LayoutInflater.from(parent.context).inflate(R.layout.card_user, parent, false)
//        itemView.setOnClickListener {
//            /**
//             * Before using safeargs and directions
//             * it.findNavController().navigate(R.id.action_navigation_friends_to_navigation_chat)
//             */
//            it.findNavController().navigate(FriendsFragmentDirections.actionNavigationFriendsToNavigationChat())
//        }
//        return FriendViewHolder(
//            itemView
//        )
//    }
//
//    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
//        val currentFriend = getItem(position)
//        holder.userUsername.text = currentFriend.username
//        holder.userStatus.text = "Status"
//        Glide.with(holder.userProfileImage.context)
//            .load(currentFriend.profileImage)
//            .into(holder.userProfileImage)
//    }
//
//    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val userProfileImage: ImageView = itemView.findViewById(R.id.user_profile_image)
//        val userUsername: TextView = itemView.findViewById(R.id.user_username)
//        val userStatus: TextView = itemView.findViewById(R.id.user_status)
//    }
//}
//
//class FriendDiffCallback : DiffUtil.ItemCallback<Friend>() {
//
//    override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
//        return oldItem.uid == newItem.uid
//    }
//
//    override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
//        return oldItem == newItem
//    }
//}
