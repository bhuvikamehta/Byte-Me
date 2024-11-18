class Review {
    private String customerId;
    private String itemId;
    private int rating;
    private String comment;

    public Review(String customerId, String itemId, int rating, String comment) {
        this.customerId = customerId;
        this.itemId = itemId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getCustomerId() { return customerId; }
    public String getItemId() { return itemId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
}

