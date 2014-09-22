namespace ReactiveDotNet.messages {
    class ScanMessage
    {
        public string Url { get; private set; }

        public ScanMessage(string url)
        {
            Url = url;
        }
    }
}
