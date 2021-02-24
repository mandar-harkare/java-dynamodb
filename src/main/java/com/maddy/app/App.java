package com.maddy.app;

import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.client.builder.*;
import com.amazonaws.services.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        createTable();
        System.out.println( "Hello World!" );
    }

    public static void createTable() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
        new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1"))
        .build(); 
        DynamoDB dynamoDB = new DynamoDB(client);
        List<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));
        
        String tableName = "Movies";
        List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));

        CreateTableRequest request = new CreateTableRequest()
                .withTableName(tableName)
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                    .withReadCapacityUnits(5L)
                    .withWriteCapacityUnits(6L));

        Table table = dynamoDB.createTable(request);

        // table.waitForActive();
    }


    public static void getItem() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
        new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1"))
        .build(); 
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Movies");

        Item item = table.getItem("Id", 210);
    }

    public static void putItem() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
        new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1"))
        .build(); 
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Movies");

        // Build a list of related items
        List<Number> relatedItems = new ArrayList<Number>();
        relatedItems.add(341);
        relatedItems.add(472);
        relatedItems.add(649);

        //Build a map of product pictures
        Map<String, String> pictures = new HashMap<String, String>();
        pictures.put("FrontView", "http://example.com/products/123_front.jpg");
        pictures.put("RearView", "http://example.com/products/123_rear.jpg");
        pictures.put("SideView", "http://example.com/products/123_left_side.jpg");

        //Build a map of product reviews
        Map<String, List<String>> reviews = new HashMap<String, List<String>>();

        List<String> fiveStarReviews = new ArrayList<String>();
        fiveStarReviews.add("Excellent! Can't recommend it highly enough!  Buy it!");
        fiveStarReviews.add("Do yourself a favor and buy this");
        reviews.put("FiveStar", fiveStarReviews);

        List<String> oneStarReviews = new ArrayList<String>();
        oneStarReviews.add("Terrible product!  Do not buy this.");
        reviews.put("OneStar", oneStarReviews);

        // Build the item
        Item item = new Item()
            .withPrimaryKey("Id", 123)
            .withString("Title", "Bicycle 123")
            .withString("Description", "123 description")
            .withString("BicycleType", "Hybrid")
            .withString("Brand", "Brand-Company C")
            .withNumber("Price", 500)
            .withStringSet("Color",  new HashSet<String>(Arrays.asList("Red", "Black")))
            .withString("ProductCategory", "Bicycle")
            .withBoolean("InStock", true)
            .withNull("QuantityOnHand")
            .withList("RelatedItems", relatedItems)
            .withMap("Pictures", pictures)
            .withMap("Reviews", reviews);

        // Write the item to the table
        PutItemOutcome outcome = table.putItem(item);
    }
}
