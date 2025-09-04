package com.example.bottomnavigationbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomnavigationbar.databinding.FragmentMainAryanBinding
import com.example.bottomnavigationbar.sampledata.AryanChat

class Aryan : Fragment() {

    private var _binding: FragmentMainAryanBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AryanChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainAryanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AryanChatAdapter(mutableListOf())
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatRecyclerView.adapter = adapter

        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString()
            if (message.isNotBlank()) {
                adapter.addMessage(AryanChat(message, true))
                adapter.addMessage(AryanChat("Isac", false))  // Aryan bubble
                binding.messageInput.text.clear()
                binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
