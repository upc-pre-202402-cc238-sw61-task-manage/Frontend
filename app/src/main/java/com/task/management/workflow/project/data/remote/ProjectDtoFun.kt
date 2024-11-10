package com.task.management.workflow.project.data.remote

import com.task.management.workflow.project.domain.Project

fun ProjectDto.toProject() = Project(projectId, title, description, leader)