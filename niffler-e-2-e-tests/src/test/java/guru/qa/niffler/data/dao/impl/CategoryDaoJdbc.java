package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.CategoryEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {
    private static final Config CFG = Config.getInstance();

    @Override
    public CategoryEntity create(CategoryEntity category) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO category (username, name, archived) " +
                            "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, category.getUsername());
                ps.setString(2, category.getName());
                ps.setBoolean(3, category.isArchived());

                ps.executeUpdate();

                final UUID generatedKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can`t find id in ResultSet");
                    }
                }
                category.setId(generatedKey);
                return category;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM category WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        CategoryEntity ce = new CategoryEntity();
                        ce.setId(rs.getObject("id", UUID.class));
                        ce.setUsername(rs.getString("username"));
                        ce.setName(rs.getString("name"));
                        ce.setArchived(rs.getBoolean("archived"));
                        return Optional.of(ce);
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
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        String query = "SELECT * FROM category WHERE username = ? AND name = ?";
        return findCategory(query, ps -> {
            ps.setString(1, username);
            ps.setString(2, categoryName);
        });
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        List<CategoryEntity> categories = new ArrayList<>();
        String query = "SELECT * FROM category WHERE username = ?";
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(mapRowToCategoryEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving categories", e);
        }
        return categories;
    }

    @Override
    public void deleteCategory(CategoryEntity category) {
        String query = "DELETE FROM category WHERE id = ?";
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setObject(1, category.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting category", e);
        }
    }

    @Override
    public CategoryEntity updateCategory(CategoryEntity category) {
        String query = "UPDATE category SET name = ?, archived = ? WHERE id = ?";
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, category.getName());
            ps.setBoolean(2, category.isArchived());
            ps.setObject(3, category.getId());
            ps.executeUpdate();
            return category;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating category", e);
        }
    }

    private Optional<CategoryEntity> findCategory(String query, PreparedStatementSetter setter) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(query)) {

            setter.setParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCategoryEntity(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding category", e);
        }
    }


    private CategoryEntity mapRowToCategoryEntity(ResultSet rs) throws SQLException {
        CategoryEntity ce = new CategoryEntity();
        ce.setId(rs.getObject("id", UUID.class));
        ce.setUsername(rs.getString("username"));
        ce.setName(rs.getString("name"));
        ce.setArchived(rs.getBoolean("archived"));
        return ce;
    }

    @FunctionalInterface
    private interface PreparedStatementSetter {
        void setParameters(PreparedStatement ps) throws SQLException;
    }
}
