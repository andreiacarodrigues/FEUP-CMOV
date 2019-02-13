using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace CMOV2_Xamarin
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class CompanyAnalysis : ContentPage
	{
        public ListView ListView;

        public CompanyAnalysis ()
		{
			InitializeComponent ();
            BindingContext = new MyCompanyAnalysisViewModel();
            ListView = MenuItemsListView;

            ListView.ItemSelected += OnSelection;
        }

        private void OnSelection(object sender, SelectedItemChangedEventArgs e)
        {
            if (e.SelectedItem == null)
            {
                return;
            }
            else
            {
                Company c = (Company)e.SelectedItem;
                Navigation.PushAsync(new CompanyPage(c));
            }
        }
    }

    class MyCompanyAnalysisViewModel : INotifyPropertyChanged
    {
        public List<Company> MenuItems { get; set; }

        public MyCompanyAnalysisViewModel()
        {
            MenuItems = Utils.CompaniesList;
        }

        #region INotifyPropertyChanged Implementation
        public event PropertyChangedEventHandler PropertyChanged;
        void OnPropertyChanged([CallerMemberName] string propertyName = "")
        {
            if (PropertyChanged == null)
                return;

            PropertyChanged.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
        #endregion
    }
}