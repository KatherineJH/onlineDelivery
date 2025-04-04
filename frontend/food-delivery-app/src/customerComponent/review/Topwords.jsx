import React, { useEffect, useState } from "react";
import axios from "axios";
import { Box, Typography, Grid, Paper } from "@mui/material";

const TopWords = () => {
  const [topWords, setTopWords] = useState([]);
  const [worstWords, setWorstWords] = useState([]);

  useEffect(() => {
    const fetchWords = async () => {
      try {
        const topResponse = await axios.get("http://localhost:5000/top-words");
        const worstResponse = await axios.get(
          "http://localhost:5000/worst-words"
        );
        setTopWords(topResponse.data.top_influential_words);
        setWorstWords(worstResponse.data.bottom_influential_words);
      } catch (error) {
        console.error("Error fetching words:", error);
      }
    };
    fetchWords();
  }, []);

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h4" gutterBottom>
        Top Influential Words
      </Typography>
      <Grid container spacing={4}>
        <Grid item xs={12} md={6}>
          <Paper elevation={3} sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Top 20 Positive Words
            </Typography>
            <ul>
              {topWords.map((word, idx) => (
                <li key={idx}>{word}</li>
              ))}
            </ul>
          </Paper>
        </Grid>
        <Grid item xs={12} md={6}>
          <Paper elevation={3} sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Top 20 Negative Words
            </Typography>
            <ul>
              {worstWords.map((word, idx) => (
                <li key={idx}>{word}</li>
              ))}
            </ul>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default TopWords;
