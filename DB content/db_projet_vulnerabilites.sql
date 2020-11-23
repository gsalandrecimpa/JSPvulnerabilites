-- phpMyAdmin SQL Dump
-- version 4.1.4
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Dim 22 Novembre 2020 à 15:43
-- Version du serveur :  5.6.15-log
-- Version de PHP :  5.5.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `db_projet_vulnerabilites`
--

-- --------------------------------------------------------

--
-- Structure de la table `logiciel`
--

DROP TABLE IF EXISTS logiciel;
CREATE TABLE  `logiciel` (
  `nom_logiciel` varchar(50) COLLATE utf8_general_mysql500_ci NOT NULL,
  PRIMARY KEY (`nom_logiciel`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci;

-- --------------------------------------------------------

--
-- Structure de la table `solution`
--

DROP TABLE IF EXISTS solution;
CREATE TABLE  `solution` (
  `id_sol` int(11) NOT NULL AUTO_INCREMENT,
  `reference` varchar(20) COLLATE utf8_general_mysql500_ci NOT NULL,
  `titre` varchar(50) COLLATE utf8_general_mysql500_ci NOT NULL,
  `description` varchar(1000) COLLATE utf8_general_mysql500_ci NOT NULL,
  PRIMARY KEY (`id_sol`),
  UNIQUE KEY `reference` (`reference`)
) ENGINE=INNODB  DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci AUTO_INCREMENT=1 ;


--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS user;
CREATE TABLE  `user` (
  `username` varchar(50) COLLATE utf8_general_mysql500_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_general_mysql500_ci NOT NULL,
  `role` varchar(10) COLLATE utf8_general_mysql500_ci NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci;


-- --------------------------------------------------------


--
-- Structure de la table `vulnerabilite`
--

DROP TABLE IF EXISTS vulnerabilite;
CREATE TABLE  `vulnerabilite` (
  `id_vuln` int(11) NOT NULL AUTO_INCREMENT,
  `reference` varchar(20) COLLATE utf8_general_mysql500_ci NOT NULL,
  `titre` varchar(50) COLLATE utf8_general_mysql500_ci NOT NULL,
  `gravite` int(11) NOT NULL,
  `description` varchar(1000) COLLATE utf8_general_mysql500_ci NOT NULL,
  PRIMARY KEY (`id_vuln`),
  UNIQUE KEY `reference` (`reference`)
) ENGINE=INNODB  DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------
--
-- Structure de la table `logiciel_solution`
--

DROP TABLE IF EXISTS logiciel_solution;
CREATE TABLE  `logiciel_solution` (
  `id_sol` int(11) NOT NULL,
  `nom_logiciel` varchar(50) COLLATE utf8_general_mysql500_ci NOT NULL,
    FOREIGN KEY (id_sol) REFERENCES solution (id_sol) ON UPDATE RESTRICT ON DELETE CASCADE,
    FOREIGN KEY (nom_logiciel) REFERENCES logiciel (nom_logiciel) ON UPDATE RESTRICT ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci;

-- --------------------------------------------------------
--
-- Structure de la table `user_logiciel`
--

DROP TABLE IF EXISTS user_logiciel;
CREATE TABLE  `user_logiciel` (
  `username` varchar(50) COLLATE utf8_general_mysql500_ci NOT NULL,
  `nom_logiciel` varchar(50) COLLATE utf8_general_mysql500_ci NOT NULL,
    FOREIGN KEY (username) REFERENCES user (username) ON UPDATE RESTRICT ON DELETE CASCADE,
    FOREIGN KEY (nom_logiciel) REFERENCES logiciel (nom_logiciel) ON UPDATE RESTRICT ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci;

-- --------------------------------------------------------


--
-- Structure de la table `vulnerabilite_logiciel`
--

DROP TABLE IF EXISTS vulnerabilite_logiciel;
CREATE TABLE  `vulnerabilite_logiciel` (
  `id_vuln` int(11) NOT NULL,
  `nom_logiciel` varchar(50) COLLATE utf8_general_mysql500_ci NOT NULL,
    FOREIGN KEY (id_vuln) REFERENCES vulnerabilite (id_vuln) ON UPDATE RESTRICT ON DELETE CASCADE,
    FOREIGN KEY (nom_logiciel) REFERENCES logiciel (nom_logiciel) ON UPDATE RESTRICT ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci;

-- --------------------------------------------------------

--
-- Structure de la table `vulnerabilite_solution`
--

DROP TABLE IF EXISTS vulnerabilite_solution;
CREATE TABLE  `vulnerabilite_solution` (
  `id_vuln` int(11) NOT NULL,
  `id_sol` int(11) NOT NULL,
    FOREIGN KEY (id_vuln) REFERENCES vulnerabilite (id_vuln) ON UPDATE RESTRICT ON DELETE CASCADE,
    FOREIGN KEY (id_sol) REFERENCES solution (id_sol) ON UPDATE RESTRICT ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_general_mysql500_ci;


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
