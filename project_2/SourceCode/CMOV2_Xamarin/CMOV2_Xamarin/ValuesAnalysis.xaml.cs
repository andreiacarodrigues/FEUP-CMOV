using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace CMOV2_Xamarin
{
    public partial class ValuesAnalysis : ContentPage
    {
        private List<string> companiesList = new List<string>();
        
        private string selected_company_1;
        private string quote;

        private Picker picker_comp_1;
        private Picker picker_quote;


        public ValuesAnalysis()
        {
            InitializeComponent();
         
            picker_comp_1 = this.FindByName<Picker>("company_picker_1");
           
            picker_quote = this.FindByName<Picker>("quotes_picker");

            foreach (var company in Utils.CompaniesList)
            {
                companiesList.Add(company.Name);
            }

            picker_comp_1.ItemsSource = companiesList.OrderBy(q => q).ToList();
            // Fonts

            Label app_title = this.FindByName<Label>("app_title");

            app_title.FontFamily =
                Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;

            information.FontFamily =
                Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;

            company_1.FontFamily =
               Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
               Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;

            company_picker_1.FontFamily =
               Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
               Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;

            quotes.FontFamily =
               Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
               Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;

            quotes_picker.FontFamily =
              Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
              Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;

            error.FontFamily =
              Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
              Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;

            analysis.FontFamily =
              Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
              Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;

            picker_comp_1.HorizontalOptions = 
                Device.RuntimePlatform == Device.Android ? LayoutOptions.CenterAndExpand :
                Device.RuntimePlatform == Device.UWP ? LayoutOptions.Center : LayoutOptions.Center;
            
            quotes_picker.HorizontalOptions =
                Device.RuntimePlatform == Device.Android ? LayoutOptions.CenterAndExpand :
                Device.RuntimePlatform == Device.UWP ? LayoutOptions.Center : LayoutOptions.Center;

            container.Padding = new Thickness(15, 60, 15, 50);

            analysis.Padding = Device.RuntimePlatform == Device.Android ? new Thickness(5) :
               Device.RuntimePlatform == Device.UWP ? new Thickness(13, 8, 13, 8) : new Thickness(13, 8, 13, 8);

            analysis.HorizontalOptions = Device.RuntimePlatform == Device.Android ? LayoutOptions.FillAndExpand :
               Device.RuntimePlatform == Device.UWP ? LayoutOptions.Center : LayoutOptions.Center;
        }

        private void OnPickerSelectedIndexChanged(object sender, EventArgs e)
        {
            Picker picker = sender as Picker;
            if (picker.Equals(picker_comp_1))
            {
                selected_company_1 = picker.SelectedItem.ToString();
            }
            else
            {
                quote = picker.SelectedItem.ToString();
            }
        }

        private void OnButtonClicked(object sender, EventArgs args)
        {

            if (selected_company_1 != null && selected_company_1 != "")
            {
                if (quote != null && quote != "")
                {
                    Navigation.PushAsync(new Analysis(selected_company_1, quote));
                }
                else
                {
                    //erro não escolheu a quote
                    error.Text = "You need to pick the quote extension to generate the graph relative to its close values.";
                }  
               
            }
            else
            {
                // não pickou o 1º - erro
                error.Text = "You need to pick the first company to generate the graph relative to its close values.";
            }

        }
    }
}
