import React, { useState } from "react";
import axios from "axios";
import {
  Box,
  Button,
  TextField,
  Typography,
  List,
  ListItem,
  ListItemText,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
} from "@mui/material";

const defaultReviews = [
  "Service was fantastic, and I will come back.",
  "I hated the meal. It was cold and tasteless.",
  "Loved the spicy noodles and fresh ingredients!",
  "The food was amazing and the staff were so friendly!",
];

const defaultRatings = ["Positive", "Negative", "Positive", "Positive"];

const RankedRestaurant = () => {
  const [reviews, setReviews] = useState(defaultReviews);
  const [ratings, setRatings] = useState(defaultRatings);
  const [rankedData, setRankedData] = useState([]);

  const handleChange = (index, field, value) => {
    const updater = field === "review" ? [...reviews] : [...ratings];
    updater[index] = value;
    field === "review" ? setReviews(updater) : setRatings(updater);
  };

  const handleSubmit = async () => {
    try {
      const payload = {
        reviews,
        ratings,
      };
      const response = await axios.post(
        "http://localhost:5000/rank-restaurants",
        payload
      );
      setRankedData(response.data.restaurant_ranking);
    } catch (error) {
      console.error("Ranking error:", error);
    }
  };

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" gutterBottom>
        Restaurant Ranking Based on Reviews
      </Typography>

      {reviews.map((_, index) => (
        <Box key={index} sx={{ mb: 3 }}>
          <TextField
            label={`Review ${index + 1}`}
            fullWidth
            value={reviews[index]}
            onChange={(e) => handleChange(index, "review", e.target.value)}
            sx={{ mb: 1 }}
          />
          <FormControl fullWidth>
            <InputLabel>Rating</InputLabel>
            <Select
              value={ratings[index]}
              label="Rating"
              onChange={(e) => handleChange(index, "rating", e.target.value)}
            >
              <MenuItem value="Positive">Positive</MenuItem>
              <MenuItem value="Negative">Negative</MenuItem>
            </Select>
          </FormControl>
        </Box>
      ))}

      <Button variant="contained" onClick={handleSubmit}>
        Rank Restaurants
      </Button>

      {rankedData.length > 0 && (
        <Box sx={{ mt: 4 }}>
          <Typography variant="h6" gutterBottom>
            Ranked Results:
          </Typography>
          <List>
            {rankedData.map((item, idx) => (
              <ListItem key={idx} alignItems="flex-start">
                <ListItemText
                  primary={`Rank ${item.rank}: ${item.restaurant_name}`}
                  secondary={
                    <>
                      <Typography
                        variant="body2"
                        component="span"
                        color="text.primary"
                      >
                        Review: {item.original_review}
                      </Typography>
                      <br />
                      <Typography
                        variant="body2"
                        component="span"
                        color="text.secondary"
                      >
                        Rating: {item.original_rating}
                      </Typography>
                      <br />
                      <Typography
                        variant="body2"
                        component="span"
                        color="text.secondary"
                      >
                        Score: {item.score}
                      </Typography>
                    </>
                  }
                />
              </ListItem>
            ))}
          </List>
        </Box>
      )}
    </Box>
  );
};

export default RankedRestaurant;
