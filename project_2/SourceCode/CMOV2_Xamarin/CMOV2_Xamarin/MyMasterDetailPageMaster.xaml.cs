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
    public partial class MyMasterDetailPageMaster : ContentPage
    {
        public ListView ListView;

        public MyMasterDetailPageMaster()
        {
            InitializeComponent();

            BindingContext = new MyMasterDetailPageMasterViewModel();
            ListView = MenuItemsListView;
        }

        class MyMasterDetailPageMasterViewModel : INotifyPropertyChanged
        {
            public ObservableCollection<MyMasterDetailPageMenuItem> MenuItems { get; set; }
            
            public MyMasterDetailPageMasterViewModel()
            {
                MenuItems = new ObservableCollection<MyMasterDetailPageMenuItem>(new[]
                {
                    new MyMasterDetailPageMenuItem { Id = 1, Title = "Stock Analysis", TargetType = typeof(MainPage),
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                        Logo = "stock.png"
                    },
                    new MyMasterDetailPageMenuItem { Id = 0, Title = "Company Analysis",  TargetType = typeof(CompanyAnalysis),
                        Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                        Logo = "company.png"
                    },
                    new MyMasterDetailPageMenuItem { Id = 2, Title = "Open/Close Values Analysis", TargetType = typeof(ValuesAnalysis),
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                        Logo = "values.png"
                    }
                });
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
}