namespace ReactiveDotNet.messages {
    class ScanRequest
    {
        public string Url { get; private set; }

        public ScanRequest(string url)
        {
            Url = url;
        }
    }
}
