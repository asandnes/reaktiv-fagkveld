using System.IO;
using Akka.Actor;
using ReactiveDotNet.messages;

namespace ReactiveDotNet {
    class KlientAnsvarlig : ReceiveActor
    {
        private readonly string domain;

        public KlientAnsvarlig(string domain)
        {
            this.domain = domain;
            Receive<ScanMessage>(HandleScanMessage);
            Receive<ImageResult>(HandleImageResult);
            Receive<StopRequest>(HandleStopRequest);
        }

        private bool HandleScanMessage(ScanMessage msg)
        {
            WriteLog("Starting scan of " + msg.Url);

            // TODO: Start page scanning actor.
            return true;
        }

        private bool HandleImageResult(ImageResult msg)
        {
            WriteLog("Image found: " + msg.ImageUrl);
            return true;
        }

        private bool HandleStopRequest(StopRequest msg)
        {
            WriteLog("Stopping scan...");
            return true;
        }

        private void WriteLog(string line)
        {
            using (StreamWriter outputFile = new StreamWriter("output.txt", append: true))
            {
                outputFile.WriteLineAsync(line);
            }
        }
    }
}
