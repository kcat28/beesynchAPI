-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Feb 19, 2025 at 10:33 AM
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
  `bill_status` varchar(100) NOT NULL,
  `img_path` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `bills`
--

INSERT INTO `bills` (`bill_id`, `hive_id`, `bill_name`, `amount`, `description`, `bill_status`, `img_path`) VALUES
(12, 1, 'Electricity Bill', 250.75, 'Monthly electricity bill for the hive', 'Pending', '/images/electricity_bill.jpg'),
(13, 1, 'Electricity Bill', 250.75, 'Monthly electricity bill for the hive', 'Pending', '/images/electricity_bill.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `hive`
--

CREATE TABLE `hive` (
  `hive_id` int(11) NOT NULL,
  `hive_name` varchar(100) NOT NULL,
  `hive_created_date` date NOT NULL,
  `img_path` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hive`
--

INSERT INTO `hive` (`hive_id`, `hive_name`, `hive_created_date`, `img_path`) VALUES
(1, 'test hive', '2025-01-11', NULL),
(3, 'Main Hive', '2025-02-19', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `household_members`
--

CREATE TABLE `household_members` (
  `user_id` int(11) NOT NULL,
  `hive_id` int(11) NOT NULL,
  `ranking_id` int(11) DEFAULT NULL,
  `role` varchar(200) NOT NULL,
  `points` int(11) DEFAULT NULL,
  `achievements` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `household_members`
--

INSERT INTO `household_members` (`user_id`, `hive_id`, `ranking_id`, `role`, `points`, `achievements`) VALUES
(1, 1, 6, 'Member', 0, 'null'),
(2, 1, NULL, 'member', 100, 'null');

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
(1, 169, 1, 'New task Created: Pls cuh', '2025-01-21'),
(2, 170, 2, 'New Bill Created: Electricity Bill', '2025-01-22'),
(67, 171, 1, 'New task Created: checking for sched', '2025-02-10'),
(68, 172, 1, 'New task Created: checking for sched', '2025-02-13'),
(69, 173, NULL, 'New Bill Created: Electricity Bill', '2025-02-13'),
(70, 174, 1, 'New task Created: checking for sched', '2025-02-13');

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
(6, 1, 1, 1, '2025-01-31', '2025-02-02');

--
-- Triggers `ranking`
--
DELIMITER $$
CREATE TRIGGER `after_ranking_insert` AFTER INSERT ON `ranking` FOR EACH ROW BEGIN
    UPDATE household_members 
    SET ranking_id = NEW.ranking_id 
    WHERE user_id = NEW.user_id AND ranking_id IS NULL;
END
$$
DELIMITER ;

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
(169, 135, NULL, 1, '2025-01-20', '2025-01-20', 'Once', '09:25:25'),
(170, NULL, 12, NULL, '2025-01-30', '2025-01-30', 'Once', '19:10:00'),
(171, 136, NULL, 1, '2024-02-10', '2024-02-17', 'Daily', '06:00:00'),
(172, 137, NULL, 1, '2024-02-10', '2024-02-17', 'Daily', '06:00:00'),
(173, NULL, 13, NULL, '2025-01-30', '2025-01-30', 'Once', '19:10:00'),
(174, 138, NULL, 1, '2024-02-10', '2024-02-17', 'Daily', '06:00:00');

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
(135, 'Pls cuh', 'asdasdasdas', 'Task', 'Completed', 10, NULL, 'E:/XAMPP/htdocs/uploads/Pls cuh.jpg'),
(136, 'checking for sched', 'This is a sample task description.', 'General', 'Ongoing', 1, NULL, 'sadas'),
(137, 'checking for sched', 'This is a sample task description.', 'General', 'Ongoing', 1, NULL, 'sadas'),
(138, 'checking for sched', 'This is a sample task description.', 'General', 'Ongoing', 1, NULL, 'sadas');

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
(176, 135, 1, '2025-01-20'),
(177, 136, 1, '2024-02-10'),
(178, 136, 2, '2024-02-10'),
(179, 137, 1, '2024-02-10'),
(180, 137, 2, '2024-02-10'),
(181, 138, 1, '2024-02-10'),
(182, 138, 2, '2024-02-10');

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
  `user_password` varchar(200) NOT NULL,
  `img_path` varchar(300) DEFAULT NULL,
  `recovery_code` varchar(255) NOT NULL,
  `is_admin` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `user_name`, `user_email`, `user_password`, `img_path`, `recovery_code`, `is_admin`) VALUES
(1, 'John Efren', 'Gannaban', 'kcat28', 'jefjef1412@gmail.com', '12345', NULL, '', 1),
(2, 'Diana Nicole', 'Danga', 'dangsyana', 'dndanga37@gmail.com', '5555', NULL, '', 1),
(7, 'Jascent Pearl', 'Navarro', 'Jassy', 'jassy@gmail.com', '1234', 'test.jpgasdas', '', 1),
(8, 'Joyce Anne', 'Colocado', 'joyce', 'joyce@gmail.com', '1234', '', '1c478592-873c-4164-8040-dfe1de7e4b5c', 1),
(9, 'Keeper', 'Cat', 'Kcat28', 'keeper@gmail.com', '$2a$10$evPDXfAlHyGRkipgu2roZuXvFeS8BnKhYdki.9/XzF69RJoySyMv6', 'sampsop', 'af582595-387b-4db0-b730-42c92cc80ed9', 1);

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
  MODIFY `bill_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `hive`
--
ALTER TABLE `hive`
  MODIFY `hive_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `notification`
--
ALTER TABLE `notification`
  MODIFY `notification_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;

--
-- AUTO_INCREMENT for table `ranking`
--
ALTER TABLE `ranking`
  MODIFY `ranking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `schedule`
--
ALTER TABLE `schedule`
  MODIFY `schedule_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=175;

--
-- AUTO_INCREMENT for table `task`
--
ALTER TABLE `task`
  MODIFY `task_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=139;

--
-- AUTO_INCREMENT for table `task_assignment`
--
ALTER TABLE `task_assignment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=183;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

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
  ADD CONSTRAINT `household_members_ibfk_3` FOREIGN KEY (`ranking_id`) REFERENCES `ranking` (`ranking_id`) ON DELETE SET NULL;

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
