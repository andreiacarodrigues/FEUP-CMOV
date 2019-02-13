using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace CMOV2_Xamarin
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class CompanyPage : ContentPage
	{
        Company company;
		public CompanyPage (Company c)
		{
			InitializeComponent ();
            company = c;
            GetData();
        }

        private async Task GetData()
        {
            string startDate = "2018-01-01";
            company_name.Text = company.Name;
            company_name.Margin = new Thickness(0, 15, 0, 5);
            company_name.FontFamily =
                Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;

            back.FontFamily =
             Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
             Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;

            back.Padding = Device.RuntimePlatform == Device.Android ? new Thickness(5) :
                Device.RuntimePlatform == Device.UWP ? new Thickness(13, 8, 13, 8) : new Thickness(13, 8, 13, 8);

            back.HorizontalOptions = Device.RuntimePlatform == Device.Android ? LayoutOptions.FillAndExpand :
               Device.RuntimePlatform == Device.UWP ? LayoutOptions.Center : LayoutOptions.Center;

            back.Margin = new Thickness(5, 0, 5, 5);

            symbol_title.FontFamily =
                        Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;
            symbol_title.TextColor = Color.FromHex("#191919");
            name_title.FontFamily =
                       Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                       Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;
            name_title.TextColor = Color.FromHex("#191919");
            industry_title.FontFamily =
                       Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                       Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;
            industry_title.TextColor = Color.FromHex("#191919");
            website_title.FontFamily =
                       Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                       Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;
            website_title.TextColor = Color.FromHex("#191919");
            description_title.FontFamily =
                       Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                       Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;
            description_title.TextColor = Color.FromHex("#191919");
            ceo_title.FontFamily =
                       Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                       Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;
            ceo_title.TextColor = Color.FromHex("#191919");
            sector_title.FontFamily =
                       Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                       Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;
            sector_title.TextColor = Color.FromHex("#191919");
            logo_title.FontFamily =
                       Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                       Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;
            logo_title.TextColor = Color.FromHex("#191919");
            symbol.FontFamily =
                        Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;
            symbol.TextColor = Color.FromHex("#191919");
            name.FontFamily =
                        Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;
            name.TextColor = Color.FromHex("#191919");
            industry.FontFamily =
                        Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;
            industry.TextColor = Color.FromHex("#191919");
            website.FontFamily =
                        Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;
            website.TextColor = Color.FromHex("#191919");
            description.FontFamily =
                        Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;
            description.TextColor = Color.FromHex("#191919");
            ceo.FontFamily =
                        Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;
            ceo.TextColor = Color.FromHex("#191919");
            sector.FontFamily =
                        Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;
            sector.TextColor = Color.FromHex("#191919");


            HttpClient client = new HttpClient();
            var response = await client.GetStringAsync("https://api.iextrading.com/1.0/stock/" + company.Tick + "/company");
            var company_data = JObject.Parse(response);

            symbol.Text = (string)company_data["symbol"];
            name.Text = (string)company_data["companyName"];
            industry.Text = (string)company_data["industry"];
            website.Text = (string)company_data["website"];
            description.Text = (string)company_data["description"];
            ceo.Text = (string)company_data["CEO"];
            sector.Text = (string)company_data["sector"];

            logo.Source = (string)company_data["symbol"] + ".png";
        }

        private void OnButtonClicked(object sender, EventArgs args)
        {
            Navigation.PopAsync();
        }
    }
}