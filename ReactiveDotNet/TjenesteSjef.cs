using System;
using Akka.Actor;
using ReactiveDotNet.messages;

namespace ReactiveDotNet {
    class TjenesteSjef : ReceiveActor
    {
        private ActorRef klientAnsvarlig;

        public TjenesteSjef()
        {
            Receive<ScanRequest>(HandleScanRequest);
            Receive<StopRequest>(HandleStopRequest);
        }

        private bool HandleScanRequest(ScanRequest msg)
        {
            var domain = GetDomainFromUrl(msg.Url);

            var props = Props.Create(() => new KlientAnsvarlig(domain));
            klientAnsvarlig = Context.ActorOf(props);

            klientAnsvarlig.Tell(new ScanMessage(msg.Url), Context.Self);

            // TODO: Remove these and make sure the actor system finds real images
            klientAnsvarlig.Tell(new ImageResult("http://messier45.com/img/halebopp.jpg", ""), Context.Self);
            klientAnsvarlig.Tell(new ImageResult("http://messier45.com/img/m31.jpg", ""), Context.Self);
            return true;
        }

        private bool HandleStopRequest(StopRequest msg)
        {
            if (klientAnsvarlig != null) klientAnsvarlig.Tell(msg, Context.Self);
            return true;
        }

        private static string GetDomainFromUrl(string url)
        {
            var uri = new Uri(url);
            var domain = uri.Host;
            return domain.StartsWith("www.") ? domain.Substring(4) : domain;
        }
    }
}
