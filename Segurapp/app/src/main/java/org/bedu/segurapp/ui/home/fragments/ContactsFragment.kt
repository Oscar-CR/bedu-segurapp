import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_contact.*
import org.bedu.segurapp.R
import org.bedu.segurapp.models.Contacts
import org.bedu.segurapp.ui.home.AddContactActivity
import org.bedu.segurapp.ui.home.adapters.ContactsAdapter

class ContactsFragment : Fragment() {
    private lateinit var mAdapter : ContactsAdapter
    private var listener : (Contacts) ->Unit = {}
    private lateinit var btn_contacts_add: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_contact, container, false)
        initComponents(view)
        onClickBtnContacts()
        return view
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
    public fun getProducts(): MutableList<Contacts>{
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

    private fun initComponents(view: View){
        btn_contacts_add = view.findViewById(R.id.btn_contacts_add)
    }

    private fun onClickBtnContacts(){
        btn_contacts_add.setOnClickListener {
            val intent=Intent(context, AddContactActivity::class.java)
            startActivity(intent)
        }
    }




}

