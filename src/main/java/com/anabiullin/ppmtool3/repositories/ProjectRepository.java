package com.anabiullin.ppmtool3.repositories;

import com.anabiullin.ppmtool3.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByProjectIdentifier(String projectId);

    @Override
    Iterable<Project> findAll();

    Iterable<Project>findAllByProjectLeader(String username);
}
