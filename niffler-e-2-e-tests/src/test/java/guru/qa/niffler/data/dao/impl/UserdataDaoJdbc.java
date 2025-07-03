package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserdataDao;
import guru.qa.niffler.data.entity.spend.UserdataEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserdataDaoJdbc implements UserdataDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserdataEntity createUser(UserdataEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO user (id, username, currency, firstname, surname, fullname, photo, photoSmall) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setObject(1, user.getId());
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getCurrency().name());
                ps.setString(4, user.getFirstname());
                ps.setString(5, user.getSurname());
                ps.setString(6, user.getFullname());
                ps.setBytes(7, user.getPhoto());
                ps.setBytes(8, user.getPhotoSmall());

                ps.executeUpdate();

                final UUID generatedKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can`t find id in ResultSet");
                    }
                }
                user.setId(generatedKey);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserdataEntity> findById(UUID id) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM user WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserdataEntity us = new UserdataEntity();
                        us.setId(rs.getObject("id", UUID.class));
                        us.setUsername(rs.getString("username"));
                        us.setCurrency(rs.getObject("value", CurrencyValues.class));
                        us.setFirstname(rs.getString("firstname"));
                        us.setSurname(rs.getString("surname"));
                        us.setFullname(rs.getString("fullname"));
                        us.setPhoto(rs.getBytes("photo"));
                        us.setPhotoSmall(rs.getBytes("photoSmall"));
                        return Optional.of(us);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserdataEntity> findByUsername(String username) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM user WHERE username = ?"
            )) {
                ps.setObject(1, username);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserdataEntity us = new UserdataEntity();
                        us.setId(rs.getObject("id", UUID.class));
                        us.setUsername(rs.getString("username"));
                        us.setCurrency(rs.getObject("value", CurrencyValues.class));
                        us.setFirstname(rs.getString("firstname"));
                        us.setSurname(rs.getString("surname"));
                        us.setFullname(rs.getString("fullname"));
                        us.setPhoto(rs.getBytes("photo"));
                        us.setPhotoSmall(rs.getBytes("photoSmall"));
                        return Optional.of(us);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserdataEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM spend WHERE id = ?"
            )) {
                ps.setObject(1, user.getId());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("Записи не были удалены");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
