package com.example.trainbookingsystem;

import com.example.trainbookingsystem.entity.Train;
import com.example.trainbookingsystem.repository.TrainRepository;
import com.example.trainbookingsystem.service.TrainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TrainServiceTest {

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private TrainService trainService;

    private Train train;

    @BeforeEach
    void setUp() {
        train = new Train();
        train.setId(1L);
        train.setName("Express");
        train.setSource("Station A");
        train.setDestination("Station B");
        train.setBasePrice(100.0);
        train.setDiscountPercentage(10.0);
    }

    @Test
    void getAllTrains_ShouldReturnListOfTrains() {
        List<Train> trains = Arrays.asList(train);
        when(trainRepository.findAll()).thenReturn(trains);

        List<Train> result = trainService.getAllTrains();

        assertEquals(1, result.size());
        assertEquals(train.getName(), result.get(0).getName());
        verify(trainRepository, times(1)).findAll();
    }

    @Test
    void getTrainById_WhenTrainExists_ShouldReturnTrain() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));

        Optional<Train> result = trainService.getTrainById(1L);

        assertTrue(result.isPresent());
        assertEquals(train.getName(), result.get().getName());
        verify(trainRepository, times(1)).findById(1L);
    }

    @Test
    void getTrainById_WhenTrainDoesNotExist_ShouldReturnEmpty() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Train> result = trainService.getTrainById(1L);

        assertFalse(result.isPresent());
        verify(trainRepository, times(1)).findById(1L);
    }

    @Test
    void createTrain_ShouldSaveAndReturnTrain() {
        when(trainRepository.save(any(Train.class))).thenReturn(train);

        Train result = trainService.createTrain(train);

        assertNotNull(result);
        assertEquals(train.getName(), result.getName());
        verify(trainRepository, times(1)).save(train);
    }

    @Test
    void updateTrain_WhenTrainExists_ShouldUpdateAndReturnTrain() {
        Train updatedTrain = new Train();
        updatedTrain.setName("Local");
        updatedTrain.setSource("Station X");
        updatedTrain.setDestination("Station Z");
        updatedTrain.setBasePrice(120.0);
        updatedTrain.setDiscountPercentage(15.0);

        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        when(trainRepository.save(any(Train.class))).thenReturn(train);

        Train result = trainService.updateTrain(1L, updatedTrain);

        assertEquals(updatedTrain.getName(), result.getName());
        assertEquals(updatedTrain.getSource(), result.getSource());
        assertEquals(updatedTrain.getDestination(), result.getDestination());
        assertEquals(updatedTrain.getBasePrice(), result.getBasePrice());
        assertEquals(updatedTrain.getDiscountPercentage(), result.getDiscountPercentage());
        verify(trainRepository, times(1)).findById(1L);
        verify(trainRepository, times(1)).save(train);
    }

    @Test
    void updateTrain_WhenTrainDoesNotExist_ShouldThrowException() {
        when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> trainService.updateTrain(1L, train));
        verify(trainRepository, times(1)).findById(1L);
        verify(trainRepository, never()).save(any(Train.class));
    }

    @Test
    void deleteTrain_ShouldDeleteTrain() {
        doNothing().when(trainRepository).deleteById(1L);

        trainService.deleteTrain(1L);

        verify(trainRepository, times(1)).deleteById(1L);
    }
}