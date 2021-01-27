-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 10.10.40.184    Database: capacity
-- ------------------------------------------------------
-- Server version	5.7.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

use capacity;
--
-- Table structure for table `tetris_capacity_template`
--

DROP TABLE IF EXISTS `tetris_capacity_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tetris_capacity_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `update_time` datetime DEFAULT NULL,
  `uuid` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `body` longtext COLLATE utf8_bin,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `business_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `transform_params` longtext COLLATE utf8_bin,
  `task_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `description` longtext COLLATE utf8_bin,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tetris_capacity_template`
--

LOCK TABLES `tetris_capacity_template` WRITE;
/*!40000 ALTER TABLE `tetris_capacity_template` DISABLE KEYS */;
INSERT INTO `tetris_capacity_template` VALUES (1,'2020-12-10 11:12:11','8ab816f7717b4d6d9ad142435e6d5600','{\"map_sources\":[{\"index\":1,\"local_ip\":\"\",\"type\":\"udp\",\"url\":\"\",\"program_array\":[{\"media_type_once_map\":{},\"program_number\":1,\"audio_array\":[{\"pid\":514,\"decode_mode\":\"cpu\"}],\"video_array\":[{\"pid\":513,\"decode_mode\":\"cpu\"}]}]}],\"map_tasks\":[{\"index\":1,\"codec\":\"mpeg2\",\"resolution\":\"720*576\",\"bitrate\":6000,\"max_bitrate\":6000,\"ts_control\":true,\"pretreatments\":[{\"treat_type\":\"fuzzy\",\"plat\":\"cpu\",\"nv_card_idx\":0,\"fuzzys\":[{\"position\":\"50,50\",\"zone\":\"500,500\",\"fuzzy_effect\":\"fuzzy\",\"mosaic_radius\":5}]}]},{\"index\":2,\"codec\":\"audiopassby\"}],\"map_outputs\":[{\"index\":1,\"type\":\"udp\",\"rate_ctrl\":\"VBR\",\"bitrate\":8000,\"scramble_mode\":\"none\",\"scramble_key\":\"\",\"tsid_pid\":1,\"av_mode\":\"asyn\",\"pmt_int\":300,\"pcr_int\":40,\"sdt_int\":300,\"buf_init\":25,\"pat_int\":300,\"ts_mode\":\"188\",\"interlace_depth\":0,\"ajust_mode\":\"dts\",\"ts_send_cnt\":7,\"target_cnt\":1,\"pad_pid\":0,\"pat_version\":-1,\"cut_allow\":2000,\"const_output\":\"on\",\"send_control\":\"off\",\"send_gap_min\":1000,\"pcr_clock\":\"internal\",\"mode\":\"caller\",\"mtu\":1400,\"time_scale\":90000,\"vid_exist\":true,\"aud_exist\":true,\"programs\":[{\"program_number\":1,\"program_name\":\"suma\",\"provider\":\"suma\",\"character_set\":\"default\",\"pmt_pid\":101,\"pcr_pid\":100}]}]}','VIDEO_TRANS','ALL','TRANSCODE',NULL,'TRANS',NULL),(2,'2020-12-10 11:12:11','b222522b1a1f46658e17e17b9c7fc609','{\"map_sources\":[{\"index\":1,\"type\":\"file\",\"file_array\":[{\"count\":1,\"seek\":0}],\"program_array\":[{\"audio_array\":[{\"decode_mode\":\"cpu\",\"pid\":514}],\"media_type_once_map\":{},\"program_number\":1,\"video_array\":[{\"decode_mode\":\"cpu\",\"pid\":513}]}]},{\"index\":2,\"type\":\"udp_ts\",\"url\":\"\",\"localIp\":\"\",\"program_array\":[{\"audio_array\":[{\"decode_mode\":\"cpu\",\"pid\":514}],\"media_type_once_map\":{},\"program_number\":1,\"video_array\":[{\"decode_mode\":\"cpu\",\"pid\":513}]}]},{\"index\":3,\"type\":\"schedule\",\"mediaType\":\"video\",\"prev\":1,\"next\":2,\"stream_type\":\"raw\",\"output_program\":{\"program_number\":1,\"element_array\":[{\"pid\":514,\"type\":\"audio\"},{\"pid\":513,\"type\":\"video\"}]},\"program_array\":[{\"media_type_once_map\":{},\"program_number\":1,\"audio_array\":[{\"pid\":514,\"decode_mode\":\"cpu\",\"backup_mode\":\"silence\",\"cutoff_time\":400}],\"video_array\":[{\"pid\":513,\"decode_mode\":\"cpu\",\"backup_mode\":\"still_picture\",\"cutoff_time\":400}]}]}],\"map_tasks\":[{\"index\":1,\"codec\":\"mpeg2\",\"resolution\":\"720*576\",\"rc_mode\":\"cbr\",\"ratio\":\"16:9\"},{\"index\":2,\"codec\":\"mp2\",\"sample_rate\":44100}],\"map_outputs\":[{\"index\":1,\"type\":\"udp\",\"url\":\"\",\"localIp\":\"\",\"rate_ctrl\":\"VBR\",\"bitrate\":8000,\"scramble_mode\":\"none\",\"scramble_key\":\"\",\"tsid_pid\":1,\"av_mode\":\"asyn\",\"pmt_int\":300,\"pcr_int\":20,\"sdt_int\":300,\"buf_init\":25,\"pat_int\":300,\"ts_mode\":\"188\",\"interlace_depth\":0,\"ajust_mode\":\"dts\",\"ts_send_cnt\":7,\"target_cnt\":1,\"pad_pid\":0,\"pat_version\":-1,\"cut_allow\":2000,\"const_output\":\"on\",\"send_control\":\"off\",\"send_gap_min\":1000,\"pcr_clock\":\"internal\",\"mode\":\"caller\",\"mtu\":1400,\"time_scale\":90000,\"vid_exist\":true,\"aud_exist\":true,\"programs\":[{\"program_number\":1,\"program_name\":\"Suma\",\"provider\":\"suma\",\"character_set\":\"default\",\"pmt_pid\":101,\"pcr_pid\":100}]}]}','PUSH_COMMON','ALL','PUSH',NULL,'TRANS',NULL),(3,'2020-12-10 11:12:11','c2e212fb4c1f46658e17e17b9c712ac3','{\"map_outputs\":[{\"index\":1,\"type\":\"hls_record\",\"cycle_time_seconds\":0,\"split_folder_seconds\":31536000}]}','RECORD_COMMON','ALL','RECORD',NULL,'PACKAGE',NULL);
/*!40000 ALTER TABLE `tetris_capacity_template` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-01-27  9:17:50
