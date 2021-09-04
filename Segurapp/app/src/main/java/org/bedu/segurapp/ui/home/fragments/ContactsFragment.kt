import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_contact.*
import org.bedu.segurapp.R
import org.bedu.segurapp.models.Contacts
import org.bedu.segurapp.ui.home.AddContactActivity
import org.bedu.segurapp.adapters.ContactsAdapter
import android.text.Editable


class ContactsFragment : Fragment(){
    private var listener : (Contacts) ->Unit = {}
    private lateinit var btn_contacts_add: FloatingActionButton
    private lateinit var etSearchContact: EditText
    private lateinit var adapter: ContactsAdapter
    private lateinit var contactsList: MutableList<Contacts>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_contact, container, false)
        initComponents(view)
        onClickBtnContacts()
        initListener(view)

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
        recyclerCalls.layoutManager = LinearLayoutManager(context)
        adapter = ContactsAdapter(getProducts())
        //asignando el Adapter al RecyclerView
        recyclerCalls.adapter = adapter
    }


    //Generando datos
    fun getProducts(): MutableList<Contacts> {
         contactsList  = ArrayList()

         contactsList.add(Contacts(R.drawable.unknown,"Andres","5512345678"))
         contactsList.add(Contacts(R.drawable.unknown,"Diego","5598764321"))
         contactsList.add(Contacts(R.drawable.unknown,"David","5511111111"))
         contactsList.add(Contacts(R.drawable.unknown,"Brandon","5522222222"))
         contactsList.add(Contacts(R.drawable.unknown,"Ricardo","5533333333"))
         contactsList.add(Contacts(R.drawable.unknown,"Antonio","5544444444"))
         contactsList.add(Contacts(R.drawable.unknown,"Jose","5555555555"))
         contactsList.add(Contacts(R.drawable.unknown,"Armando","5566666666"))
         contactsList.add(Contacts(R.drawable.unknown,"Brenda","5577777777"))
         contactsList.add(Contacts(R.drawable.unknown,"Edith","5588888888"))

        return contactsList

    }

    private fun initComponents(view: View){
        btn_contacts_add = view.findViewById(R.id.btn_contacts_add)
        etSearchContact = view.findViewById(R.id.etSearchContact)
    }

    private fun onClickBtnContacts(){
        btn_contacts_add.setOnClickListener {
            val intent=Intent(context, AddContactActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initListener(view: View){

        etSearchContact.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {


            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })

    }

    private fun filter(text:String){

        val filteredList: MutableList<Contacts> = ArrayList()
        for (item in contactsList) {
            if (item.name.lowercase().contains(text.lowercase())) {
                filteredList.add(item)

            }
        }
        adapter.filterList(filteredList);
    }



}





