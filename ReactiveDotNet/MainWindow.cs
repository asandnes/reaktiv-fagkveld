using System;
using System.Windows.Forms;
using Akka.Actor;
using ReactiveDotNet.messages;

namespace ReactiveDotNet {
    public partial class MainWindow : Form
    {
        private readonly ActorRef mainActor;

        public MainWindow()
        {
            InitializeComponent();
            var system = ActorSystem.Create("ReaktivFagkveld");
            mainActor = system.ActorOf<TjenesteSjef>();
        }

        private void scanButton_Click(object sender, EventArgs e)
        {
            var url = urlBox.Text;
            if (IsInvalidUrl(url))
            {
                MessageBox.Show("Ugyldig URL!");
                return;
            }

            mainActor.Tell(new ScanRequest(url));
            urlBox.Text = "Scanning " + url + "...";
        }

        private void stopButton_Click(object sender, EventArgs e) {
            mainActor.Tell(new StopRequest());
            urlBox.Text = "";
            urlBox.Focus();
        }

        private bool IsInvalidUrl(string url)
        {
            try
            {
                new Uri(url);
                return false;
            }
            catch (UriFormatException)
            {
                return true;
            }
        }
    }
}
