using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using SkiaSharp;
using Syncfusion.SfChart.XForms;
using System.Net.Http;

namespace CMOV2_Xamarin
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class GraphPage : ContentPage
    {
        Company selected_company_1;
        Company selected_company_2;
        int quote;

        JObject company_data_1;
        JObject company_data_2;

        List<Point> Data_Company_1 = new List<Point>();
        List<Point> Data_Company_2 = new List<Point>();

        public GraphPage(string selected_company_1, string selected_company_2, string quote)
        {
            InitializeComponent();

            this.selected_company_1 = Utils.GetCompany(selected_company_1);
            this.selected_company_2 = Utils.GetCompany(selected_company_2);
            this.quote = int.Parse(quote.Split(' ')[0]);

            back.FontFamily =
              Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
              Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;

            back.Padding = Device.RuntimePlatform == Device.Android ? new Thickness(5) :
                Device.RuntimePlatform == Device.UWP ? new Thickness(13, 8, 13, 8) : new Thickness(13, 8, 13, 8);

            back.HorizontalOptions = Device.RuntimePlatform == Device.Android ? LayoutOptions.FillAndExpand :
               Device.RuntimePlatform == Device.UWP ? LayoutOptions.Center : LayoutOptions.Center;

            back.Margin = new Thickness(5, 0, 5, 5);

            GetData();
        }


        private async Task GetData()
        {
            string startDate = "2018-01-01";

            if (selected_company_1 != null)
            {
                HttpClient client = new HttpClient();
                var response = await client.GetStringAsync("https://marketdata.websol.barchart.com/getHistory.json?apikey=" + Utils.API_KEY +
                    "&symbol=" + selected_company_1.Tick + "&type=daily&maxRecords=" + quote + "&startDate=" + startDate);

                var data = JsonConvert.DeserializeObject(response);
                System.Diagnostics.Debug.WriteLine("DATA: " + data);

                company_data_1 = JObject.Parse(response);
            }
            if (selected_company_2 != null)
            {
                HttpClient client = new HttpClient();
                var response = await client.GetStringAsync("https://marketdata.websol.barchart.com/getHistory.json?apikey=" + Utils.API_KEY +
                    "&symbol=" + selected_company_2.Tick + "&type=daily&maxRecords=" + quote + "&startDate=" + startDate);

                var data = JsonConvert.DeserializeObject(response);
                System.Diagnostics.Debug.WriteLine("DATA: " + data);

                company_data_2 = JObject.Parse(response);
            }

            this.ConstructGraph();
        }

        private void ConstructGraph()
        {
            foreach (var result in company_data_1["results"])
            {
                string tradingDay = (string)result["tradingDay"];
                tradingDay = tradingDay.Split('-')[2] + "-" + tradingDay.Split('-')[1];
                float open = (float)result["open"];
                float high = (float)result["high"];
                float low = (float)result["low"];
                float close = (float)result["close"];

                Data_Company_1.Add(new Point() { Date = tradingDay, Close = close });
            }

            if (selected_company_2 != null)
            {
                foreach (var result in company_data_2["results"])
                {
                    string tradingDay = (string)result["tradingDay"];
                    tradingDay = tradingDay.Split('-')[2] + "-" + tradingDay.Split('-')[1];
                    System.Diagnostics.Debug.WriteLine("T DAY: " + tradingDay);
                    float open = (float)result["open"];
                    float high = (float)result["high"];
                    float low = (float)result["low"];
                    float close = (float)result["close"];

                    Data_Company_2.Add(new Point() { Date = tradingDay, Close = close });
                }
            }

            LineSeries series_1 = new LineSeries();
            series_1.Color = Color.Red;
            series_1.ItemsSource = Data_Company_1;
            series_1.XBindingPath = "Date";
            series_1.YBindingPath = "Close";
            series_1.Label = selected_company_1.Name.ToString();

            series_1.DataMarker = new ChartDataMarker();
            series_1.DataMarker.ShowLabel = false;
            series_1.EnableTooltip = true;
            chart.Legend = new ChartLegend();

            CategoryAxis date_axis = new CategoryAxis();
            date_axis.EdgeLabelsDrawingMode = EdgeLabelsDrawingMode.Fit;
            date_axis.Title.Text = "Day";
            date_axis.Title.Margin = Device.RuntimePlatform == Device.Android ? 3 :
               Device.RuntimePlatform == Device.UWP ? 10 : 10;

            date_axis.Title.FontSize = Device.RuntimePlatform == Device.Android ? 10 :
               Device.RuntimePlatform == Device.UWP ? 20 : 20;
            date_axis.Title.FontFamily = Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
             Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;

            chart.PrimaryAxis = date_axis;

            NumericalAxis close_axis = new NumericalAxis();
            close_axis.EdgeLabelsDrawingMode = EdgeLabelsDrawingMode.Fit;
            close_axis.Title.Text = "Close Value";
            close_axis.Title.Margin = Device.RuntimePlatform == Device.Android ? 3 :
              Device.RuntimePlatform == Device.UWP ? 10 : 10;
            close_axis.Title.FontSize = Device.RuntimePlatform == Device.Android ? 10 :
             Device.RuntimePlatform == Device.UWP ? 20 : 20;
            close_axis.Title.FontFamily = Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
            Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;
            close_axis.Title.Margin = 3;
            chart.SecondaryAxis = close_axis;
            chart.Series.Add(series_1);

            if (selected_company_2 != null)
            {
                LineSeries series_2 = new LineSeries();
                series_2.Color = Color.Green;
                series_2.ItemsSource = Data_Company_2;
                series_2.XBindingPath = "Date";
                series_2.YBindingPath = "Close";
                series_2.Label = selected_company_2.Name.ToString();

                series_2.DataMarker = new ChartDataMarker();
                series_2.DataMarker.ShowLabel = false;
                series_2.EnableTooltip = true;
                chart.Legend = new ChartLegend();

                chart.Series.Add(series_2);
            }

            chart.Title.Text = "Close Values for a " + quote + " Days Quote";
            chart.Title.FontFamily = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : null;

            chart.Title.FontSize = 25;

            chart.Title.TextColor = Color.DodgerBlue;


            chart.PrimaryAxis.LabelStyle.FontFamily = Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
                Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;

            chart.SecondaryAxis.LabelStyle.FontFamily = Device.RuntimePlatform == Device.Android ? "Ubuntu-Regular.ttf#Ubuntu-Regular" :
               Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Regular.ttf#Ubuntu" : null;
        }

        private void OnButtonClicked(object sender, EventArgs args)
        {
            Navigation.PopAsync();
        }
    }
}