package com.github.daggerok.employee;

import io.reactiverse.axle.pgclient.PgIterator;
import io.reactiverse.axle.pgclient.PgPool;
import io.reactiverse.axle.pgclient.Row;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "allOf")
@RequiredArgsConstructor(staticName = "of")
public class Employee {

  Long id;
  @NonNull String name;
  Integer salary, organizationId;

  private static Employee from(Row row) {
    return Employee.allOf(
        row.getLong("id"),
        row.getString("name"),
        row.getInteger("salary"),
        row.getInteger("organization_id")
    );
  }

  public static CompletionStage<List<Employee>> findAll(PgPool client) {
    return client.query("SELECT id, name, salary, organization_id FROM employees ORDER BY name ASC").thenApply(pgRowSet -> {
      List<Employee> list = new ArrayList<>(pgRowSet.size());
      PgIterator pgIterator = pgRowSet.iterator();
      while (pgIterator.hasNext()) {
        list.add(from(pgIterator.next()));
      }
      return list;
    });
  }
}
