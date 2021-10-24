package org.bedu.segurapp.contactList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.bedu.segurapp.CoroutineTestRule
//import org.bedu.segurapp.models.local.Contact
import org.junit.Rule

// Uncomment @Before and @Test blocks to make tests

class ContactListViewModelTest {

    /*
    private lateinit var contactRepository: IContactRepository
    private lateinit var viewModel: AddContactViewModel

    // Contacts
    private val antonio = Contact(1, "Antonio Labra", "4774955345")
    private val astrid = Contact(2, "Astrid Guerrero", "5533225596")
    private val oscar = Contact(3, "Oscar Chávez", "5621805859")
    private val regina = Contact(4, "Regina Bernal", "5611240930")

     */

    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRun = CoroutineTestRule()

    /*

        @Before
        fun setup(){
            contactRepository = FakeContactRepository()

            val contactList = listOf(astrid, oscar, regina)

            contactRepository.populateContacts(contactList)
            viewModel = AddContactViewModel(contactRepository)
        }

        @Test
        fun insertContact_insertContact(){
            val observer = Observer<List<Contact>>{}

            try {
                viewModel.contactsList.observeForever(observer)
                viewModel.addContact(antonio)

                val contacts = viewModel.contactsList.value
                assertThat(contacts).contains(antonio)

            } finally {
                viewModel.contactsList.removeObserver(observer)
            }
        }

        @Test
        fun removeContact_removeContacts(){
            val observer = Observer<List<Contact>>{}

            try {
                viewModel.contactsList.observeForever(observer)
                viewModel.removeContact(antonio)

                val contacts = viewModel.contactsList.value
                assertThat(contacts).doesNotContain(antonio)

            } finally {
                viewModel.contactsList.removeObserver(observer)
            }
        }
    */
}
