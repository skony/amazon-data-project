package pl.put.fc.file;

public enum DataFile {
    
    AMAZON_INSTANT_VIDEO("Amazon_Instant_Video.json"),
    APPS_FOR_ANDROID("Apps_for_Android.json"),
    AUTOMOTIVE("Automotive.json"),
    BABY("Baby.json"),
    BEAUTY("Beauty.json"),
    BOOKS("Books.json"),
    CDS_AND_VINYL("CDs_and_Vinyl.json"),
    CELL_PHONES_AND_ACCESSORIES("Cell_Phones_and_Accessories.json"),
    CLOTHING_SHOES_AND_JEWELRY("Clothing_Shoes_and_Jewelry.json"),
    DIGITAL_MUSIC("Digital_Music.json"),
    ELECTRONICS("Electronics.json"),
    GROCERY_AND_GOURMET_FOOD("Grocery_and_Gourmet_Food.json"),
    HEALTH_AND_PERSONAL_CARE("Health_and_Personal_Care.json"),
    HOME_AND_KITCHEN("Home_and_Kitchen.json"),
    KINDLE_STORE("Kindle_Store.json"),
    MOVIES_AND_TV("Movies_and_TV.json"),
    MUSICAL_INSTRUMENTS("Musical_Instruments.json"),
    OFFICE_PRODUCTS("Office_Products.json"),
    PATIO_LAWN_AND_GARDEN("Patio_Lawn_and_Garden.json"),
    PET_SUPPLIES("Pet_Supplies.json"),
    SPORTS_AND_OUTDOORS("Sports_and_Outdoors.json"),
    TOOLS_AND_HOME_IMPOVEMENT("Tools_and_Home_Improvement.json"),
    TOYS_AND_GAMES("Toys_and_Games.json"),
    VIDEO_GAMES("Video_Games.json");
    
    private final String dataFile;
    
    private DataFile(String dataFile) {
        this.dataFile = dataFile;
    }
    
    public String getOriginalMetaFile() {
        return "meta_" + dataFile;
    }
    
    public String getFixedMetaFile() {
        return "meta_" + dataFile.replace(".json", "_fixed.json");
    }
    
    public String getReviewFile() {
        return "reviews_" + dataFile;
    }
}
