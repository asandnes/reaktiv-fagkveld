namespace ReactiveDotNet.messages {
    class ImageResult
    {
        public string ImageUrl { get; private set; }
        public string ThumbnailUrl { get; private set; }

        public ImageResult(string imageUrl, string thumbnailUrl)
        {
            ImageUrl = imageUrl;
            ThumbnailUrl = thumbnailUrl;
        }
    }
}
