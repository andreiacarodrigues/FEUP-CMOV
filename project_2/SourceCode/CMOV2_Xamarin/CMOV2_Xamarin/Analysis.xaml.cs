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
	public partial class Analysis : ContentPage
	{
        int quote;
        Company selected_company;

        public Analysis (string selected_company, string quote)
		{
			InitializeComponent ();
            this.selected_company = Utils.GetCompany(selected_company);
            this.quote = int.Parse(quote.Split(' ')[0]);

            GetData();
        }

        private async Task GetData()
        {
            string startDate = "2018-01-01";
            company_name.Text = selected_company.Name;
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

            HttpClient client = new HttpClient();
            var response = await client.GetStringAsync("https://marketdata.websol.barchart.com/getHistory.json?apikey=" + Utils.API_KEY +
                "&symbol=" + selected_company.Tick + "&type=daily&maxRecords=" + quote + "&startDate=" + startDate);

            var data = JsonConvert.DeserializeObject(response);

            var grid = new Grid();

            grid.RowSpacing = 0;

            grid.Margin = new Thickness(0, 0, 0, 5);

            grid.ColumnDefinitions.Add(new ColumnDefinition { Width = new GridLength(1, GridUnitType.Star) });
            grid.ColumnDefinitions.Add(new ColumnDefinition { Width = new GridLength(1, GridUnitType.Absolute) });
            grid.ColumnDefinitions.Add(new ColumnDefinition { Width = new GridLength(1, GridUnitType.Star) });
            grid.ColumnDefinitions.Add(new ColumnDefinition { Width = new GridLength(1, GridUnitType.Absolute) });
            grid.ColumnDefinitions.Add(new ColumnDefinition { Width = new GridLength(1, GridUnitType.Star) });
            grid.ColumnDefinitions.Add(new ColumnDefinition { Width = new GridLength(1, GridUnitType.Absolute) });
            grid.ColumnDefinitions.Add(new ColumnDefinition { Width = new GridLength(1, GridUnitType.Star) });

            grid.RowDefinitions.Add(new RowDefinition { Height = new GridLength(1, GridUnitType.Star) });

            for (int i = 0; i < quote - 1; i++)
            {
               
                grid.RowDefinitions.Add(new RowDefinition { Height = new GridLength(1, GridUnitType.Star) });
            }

            StackLayout s = new StackLayout
            {
                BackgroundColor = Color.Black,
                HorizontalOptions = LayoutOptions.FillAndExpand,
                VerticalOptions = LayoutOptions.Fill
            };
            
          // Titulos
           grid.Children.Add(
                new Label { Text = "Date",
                    HorizontalTextAlignment = TextAlignment.Center,
                    VerticalTextAlignment = TextAlignment.Center,
                    TextColor = Color.FromHex("#191919"),
                    FontSize = 15,
                    FontFamily =
                    Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                    Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null
                }
                , 0, 0);

            StackLayout s1 = new StackLayout
            {
                BackgroundColor = Color.FromHex("#ededed"),
                HorizontalOptions = LayoutOptions.FillAndExpand,
                VerticalOptions = LayoutOptions.Fill
            };
            grid.Children.Add(s1, 1, 0);

            grid.Children.Add(
                new Label { Text = "Open Value",
                    HorizontalTextAlignment = TextAlignment.Center,
                    VerticalTextAlignment = TextAlignment.Center,
                    TextColor = Color.FromHex("#191919"),
                    FontSize = 15,
                    FontFamily =
                    Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                    Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null
                }, 2, 0);

            StackLayout s2 = new StackLayout
            {
                BackgroundColor = Color.FromHex("#ededed"),
                HorizontalOptions = LayoutOptions.FillAndExpand,
                VerticalOptions = LayoutOptions.Fill
            };
            grid.Children.Add(s2, 3, 0);

            grid.Children.Add(
                new Label { Text = "Close Value",
                    HorizontalTextAlignment = TextAlignment.Center,
                    VerticalTextAlignment = TextAlignment.Center,
                    TextColor = Color.FromHex("#191919"),
                    FontSize = 15,
                    FontFamily =
                    Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                    Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null
                }, 4, 0);

            StackLayout s3 = new StackLayout
            {
                BackgroundColor = Color.FromHex("#ededed"),
                HorizontalOptions = LayoutOptions.FillAndExpand,
                VerticalOptions = LayoutOptions.Fill
            };
            grid.Children.Add(s3, 5, 0);

            grid.Children.Add(
                new Label { Text = "Result",
                    HorizontalTextAlignment = TextAlignment.Center,
                    VerticalTextAlignment = TextAlignment.Center,
                    TextColor = Color.FromHex("#191919"),
                    FontSize = 15,
                    FontFamily =
                    Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                    Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null
                }, 6, 0);

            var company_data = JObject.Parse(response);
            var counter = 1;

            foreach (var result in company_data["results"])
            {
                string tradingDay = (string)result["tradingDay"];
                tradingDay = tradingDay.Split('-')[2] + "-" + tradingDay.Split('-')[1] + "-" + tradingDay.Split('-')[0];
                float open = (float)result["open"];
                float close = (float)result["close"];

                grid.Children.Add(
                    new Label { Text = tradingDay,
                        HorizontalTextAlignment = TextAlignment.Center,
                        VerticalTextAlignment = TextAlignment.Center,
                        TextColor = Color.FromHex("#191919"),
                        FontSize = 15,
                        FontFamily =
                        Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null
                    }, 0, counter);

                StackLayout s4 = new StackLayout
                {
                    BackgroundColor = Color.FromHex("#ededed"),
                    HorizontalOptions = LayoutOptions.FillAndExpand,
                    VerticalOptions = LayoutOptions.Fill
                };

                grid.Children.Add(s4, 1, counter);

                grid.Children.Add(
                    new Label { Text = open.ToString(),
                        HorizontalTextAlignment = TextAlignment.Center,
                        VerticalTextAlignment = TextAlignment.Center,
                        TextColor = Color.FromHex("#191919"),
                        FontSize = 15,
                        FontFamily =
                        Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null
                    }, 2, counter);

                StackLayout s5 = new StackLayout
                {
                    BackgroundColor = Color.FromHex("#ededed"),
                    HorizontalOptions = LayoutOptions.FillAndExpand,
                    VerticalOptions = LayoutOptions.Fill
                };

                grid.Children.Add(s5, 3, counter);

                grid.Children.Add(
                    new Label { Text = close.ToString(),
                        HorizontalTextAlignment = TextAlignment.Center,
                        VerticalTextAlignment = TextAlignment.Center,
                        TextColor = Color.FromHex("#191919"),
                        FontSize = 15,
                        FontFamily =
                        Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null
                    }, 4, counter);

                StackLayout s6 = new StackLayout
                {
                    BackgroundColor = Color.FromHex("#ededed"),
                    HorizontalOptions = LayoutOptions.FillAndExpand,
                    VerticalOptions = LayoutOptions.Fill
                };
                grid.Children.Add(s6, 5, counter);

                Image res = new Image { HorizontalOptions = LayoutOptions.Center, VerticalOptions = LayoutOptions.Center, WidthRequest = 50, HeightRequest = 50 };
                if (open > close)
                {
                    res.Source = "down.png";
                }
                else
                {
                    res.Source = "up.png";
                }
                grid.Children.Add(res, 6, counter);

                counter++;
            }

            container.Children.Add(grid);
        }

        private void OnButtonClicked(object sender, EventArgs args)
        {
            Navigation.PopAsync();
        }

    }
}