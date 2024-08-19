///**
// * @author Ngoc Tram
// * @project streaming-service
// * @created 14/08/2024 - 00:54
// */
//package com.vou.streaming_service.model;
//
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import jakarta.persistence.*;
//import lombok.Getter;
//
//
//@Getter
//@Entity
//public class Option {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private String id;
//
//    private String key;
//    private String value;
//
//    private Long idQuiz;
//
//
////    @JsonCreator
////    public Option(@JsonProperty("id") String id,
////                  @JsonProperty("key") String key,
////                  @JsonProperty("value") String value
////    ) {
////        this.id = id;
////        this.key = key;
////        this.value = value;
////    }
//
//
//    @Override
//    public String toString() {
//        return "Option{" +
//                "id='" + id + '\'' +
//                ", key='" + key + '\'' +
//                ", value='" + value + '\'' +
//                '}';
//    }
//}