import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_contact.*
import org.bedu.segurapp.R
import org.bedu.segurapp.ui.home.`class`.Contacts
import org.bedu.segurapp.ui.home.adapters.ContactsAdapter

class ContactsFragment : Fragment() {
    private lateinit var mAdapter : ContactsAdapter
    private var listener : (Contacts) ->Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
    }

    fun setListener(l: (Contacts) ->Unit){
        listener = l
    }

    //configuramos lo necesario para desplegar el RecyclerView
    private fun setUpRecyclerView(){
        recyclerCalls.setHasFixedSize(true)
        recyclerCalls.layoutManager = LinearLayoutManager(activity)
        //seteando el Adapter
        mAdapter = ContactsAdapter( requireActivity(), getProducts(), listener)
        //asignando el Adapter al RecyclerView
        recyclerCalls.adapter = mAdapter
    }

    //Generando datos
    private fun getProducts(): MutableList<Contacts>{
        var contact:MutableList<Contacts> = ArrayList()

        contact.add(Contacts(R.drawable.unknown,"Andres","5512345678"))
        contact.add(Contacts(R.drawable.unknown,"Diego","5598764321"))
        contact.add(Contacts(R.drawable.unknown,"David","5511111111"))
        contact.add(Contacts(R.drawable.unknown,"Brandon","5522222222"))
        contact.add(Contacts(R.drawable.unknown,"Ricardo","5533333333"))
        contact.add(Contacts(R.drawable.unknown,"Antonio","5544444444"))
        contact.add(Contacts(R.drawable.unknown,"Jose","5555555555"))
        contact.add(Contacts(R.drawable.unknown,"Armando","5566666666"))
        contact.add(Contacts(R.drawable.unknown,"Brenda","5577777777"))
        contact.add(Contacts(R.drawable.unknown,"Edith","5588888888"))


        return contact
    }
}