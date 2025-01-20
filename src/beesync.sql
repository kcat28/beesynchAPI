-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 20, 2025 at 05:20 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `beesync`
--

-- --------------------------------------------------------

--
-- Table structure for table `bills`
--

CREATE TABLE `bills` (
  `bill_id` int(11) NOT NULL,
  `hive_id` int(11) NOT NULL,
  `bill_name` varchar(100) NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `bill_status` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hive`
--

CREATE TABLE `hive` (
  `hive_id` int(11) NOT NULL,
  `hive_name` varchar(100) NOT NULL,
  `hive_created_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hive`
--

INSERT INTO `hive` (`hive_id`, `hive_name`, `hive_created_date`) VALUES
(1, 'test hive', '2025-01-11');

-- --------------------------------------------------------

--
-- Table structure for table `household_members`
--

CREATE TABLE `household_members` (
  `user_id` int(11) NOT NULL,
  `hive_id` int(11) NOT NULL,
  `ranking_id` int(11) NOT NULL,
  `role` varchar(200) NOT NULL,
  `points` int(11) DEFAULT NULL,
  `achievements` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `household_members`
--

INSERT INTO `household_members` (`user_id`, `hive_id`, `ranking_id`, `role`, `points`, `achievements`) VALUES
(1, 1, 1, 'Member', 0, 'null'),
(2, 1, 2, 'member', 100, 'null');

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

CREATE TABLE `notification` (
  `notification_id` int(11) NOT NULL,
  `schedule_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `message` varchar(250) NOT NULL,
  `notif_created_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `notification`
--

INSERT INTO `notification` (`notification_id`, `schedule_id`, `user_id`, `message`, `notif_created_date`) VALUES
(11, 115, 1, 'New task Created: Testystsgs', '2025-01-17'),
(12, 116, 2, 'New task Created: Testystsgs', '2025-01-17'),
(13, 117, NULL, 'New task Created: Testystsgs', '2025-01-17'),
(14, 118, 1, 'New task Created: Chingxa', '2025-01-17'),
(15, 119, 2, 'New task Created: Chingxa', '2025-01-17'),
(46, 150, 1, 'New task Created: This is a test Task auto notif', '2025-01-21'),
(47, 151, 2, 'New task Created: This is a test Task auto notif', '2025-01-21');

-- --------------------------------------------------------

--
-- Table structure for table `ranking`
--

CREATE TABLE `ranking` (
  `ranking_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `hive_id` int(11) NOT NULL,
  `rank_position` int(11) DEFAULT NULL,
  `period_start` date DEFAULT NULL,
  `period_end` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ranking`
--

INSERT INTO `ranking` (`ranking_id`, `user_id`, `hive_id`, `rank_position`, `period_start`, `period_end`) VALUES
(1, 1, 1, 0, NULL, NULL),
(2, 2, 1, 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `schedule`
--

CREATE TABLE `schedule` (
  `schedule_id` int(11) NOT NULL,
  `task_id` int(11) DEFAULT NULL,
  `bill_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `start_date` date NOT NULL,
  `end_date` date DEFAULT NULL,
  `recurrence` varchar(50) DEFAULT NULL,
  `due_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `schedule`
--

INSERT INTO `schedule` (`schedule_id`, `task_id`, `bill_id`, `user_id`, `start_date`, `end_date`, `recurrence`, `due_time`) VALUES
(115, 100, NULL, 1, '2025-01-17', '2025-01-17', 'Daily', '23:35:19'),
(116, 100, NULL, 2, '2025-01-17', '2025-01-17', 'Daily', '23:35:19'),
(117, 101, NULL, NULL, '2025-01-17', '2025-01-17', 'Daily', '23:35:19'),
(118, 102, NULL, 1, '2025-01-17', '2025-01-17', 'Once', '04:25:38'),
(119, 102, NULL, 2, '2025-01-17', '2025-01-17', 'Once', '04:25:38'),
(150, 118, NULL, 1, '2023-12-01', '2023-12-31', 'Daily', '06:00:00'),
(151, 118, NULL, 2, '2023-12-01', '2023-12-31', 'Daily', '06:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE `task` (
  `task_id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `task_status` varchar(100) NOT NULL,
  `rewardpts` int(11) DEFAULT NULL,
  `completion_date` date DEFAULT NULL,
  `img_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`task_id`, `title`, `description`, `category`, `task_status`, `rewardpts`, `completion_date`, `img_path`) VALUES
(100, 'Testystsgs', NULL, 'Task', 'Ongoing', 30, NULL, ''),
(101, 'Testystsgs', NULL, 'Task', 'Ongoing', 30, NULL, ''),
(102, 'Chingxa', NULL, 'Task', 'Ongoing', 30, NULL, ''),
(118, 'This is a test Task auto notif', 'This is a sample task description.', 'General', 'Ongoing', 30, NULL, 'damn');

-- --------------------------------------------------------

--
-- Table structure for table `task_assignment`
--

CREATE TABLE `task_assignment` (
  `id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `assigned_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `task_assignment`
--

INSERT INTO `task_assignment` (`id`, `task_id`, `user_id`, `assigned_date`) VALUES
(122, 100, 1, '2025-01-17'),
(123, 100, 2, '2025-01-17'),
(124, 101, NULL, '2025-01-17'),
(125, 102, 1, '2025-01-17'),
(126, 102, 2, '2025-01-17'),
(157, 118, 1, '2023-12-01'),
(158, 118, 2, '2023-12-02');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `user_email` varchar(100) NOT NULL,
  `user_password` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `user_name`, `user_email`, `user_password`) VALUES
(1, 'John Efren', 'Gannaban', 'kcat28', 'jefjef1412@gmail.com', '12345'),
(2, 'Diana Nicole', 'Danga', 'dangsyana', 'dndanga37@gmail.com', '5555');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bills`
--
ALTER TABLE `bills`
  ADD PRIMARY KEY (`bill_id`),
  ADD KEY `hive_id` (`hive_id`);

--
-- Indexes for table `hive`
--
ALTER TABLE `hive`
  ADD PRIMARY KEY (`hive_id`);

--
-- Indexes for table `household_members`
--
ALTER TABLE `household_members`
  ADD KEY `user_id` (`user_id`),
  ADD KEY `hive_id` (`hive_id`),
  ADD KEY `ranking_id` (`ranking_id`);

--
-- Indexes for table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`notification_id`),
  ADD KEY `schedule_id` (`schedule_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `ranking`
--
ALTER TABLE `ranking`
  ADD PRIMARY KEY (`ranking_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `hive_id` (`hive_id`);

--
-- Indexes for table `schedule`
--
ALTER TABLE `schedule`
  ADD PRIMARY KEY (`schedule_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `task_id` (`task_id`) USING BTREE,
  ADD KEY `bill_id` (`bill_id`) USING BTREE;

--
-- Indexes for table `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`task_id`);

--
-- Indexes for table `task_assignment`
--
ALTER TABLE `task_assignment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `task_id` (`task_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bills`
--
ALTER TABLE `bills`
  MODIFY `bill_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `hive`
--
ALTER TABLE `hive`
  MODIFY `hive_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `notification`
--
ALTER TABLE `notification`
  MODIFY `notification_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT for table `ranking`
--
ALTER TABLE `ranking`
  MODIFY `ranking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `schedule`
--
ALTER TABLE `schedule`
  MODIFY `schedule_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=152;

--
-- AUTO_INCREMENT for table `task`
--
ALTER TABLE `task`
  MODIFY `task_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=119;

--
-- AUTO_INCREMENT for table `task_assignment`
--
ALTER TABLE `task_assignment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=159;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bills`
--
ALTER TABLE `bills`
  ADD CONSTRAINT `bills_ibfk_1` FOREIGN KEY (`hive_id`) REFERENCES `hive` (`hive_id`);

--
-- Constraints for table `household_members`
--
ALTER TABLE `household_members`
  ADD CONSTRAINT `household_members_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `household_members_ibfk_2` FOREIGN KEY (`hive_id`) REFERENCES `hive` (`hive_id`),
  ADD CONSTRAINT `household_members_ibfk_3` FOREIGN KEY (`ranking_id`) REFERENCES `ranking` (`ranking_id`);

--
-- Constraints for table `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `notification_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `ranking`
--
ALTER TABLE `ranking`
  ADD CONSTRAINT `ranking_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `ranking_ibfk_2` FOREIGN KEY (`hive_id`) REFERENCES `hive` (`hive_id`);

--
-- Constraints for table `schedule`
--
ALTER TABLE `schedule`
  ADD CONSTRAINT `schedule_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`task_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `schedule_ibfk_2` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`bill_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `schedule_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `task_assignment`
--
ALTER TABLE `task_assignment`
  ADD CONSTRAINT `task_assignment_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`task_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `task_assignment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
