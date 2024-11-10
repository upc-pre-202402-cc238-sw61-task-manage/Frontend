package com.task.management.workflow.common

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.task.management.workflow.calendar.data.remote.PackageService
import com.task.management.workflow.calendar.data.repository.PackageRepository
import com.task.management.workflow.calendar.presentation.PackageListEventsViewModel
import com.task.management.workflow.common.constants.AppServiceConstants
import com.task.management.workflow.iam.data.remote.IAMService
import com.task.management.workflow.iam.data.repository.IAMRepository
import com.task.management.workflow.iam.presentation.sign_in.SignInViewModel
import com.task.management.workflow.iam.presentation.sign_up.SignUpViewModel
import com.task.management.workflow.profiles.data.remote.ProfileService
import com.task.management.workflow.profiles.data.repository.ProfileRepository
import com.task.management.workflow.profiles.presentation.ProfilesViewModel
import com.task.management.workflow.project.data.remote.ProjectService
import com.task.management.workflow.project.data.repository.LocalProjectRepository
import com.task.management.workflow.project.data.repository.ProjectRepository
import com.task.management.workflow.project.data.repository.RemoteProjectRepository
import com.task.management.workflow.project.presentation.ProjectViewModel
import com.task.management.workflow.projectUser.data.remote.ProjectUserService
import com.task.management.workflow.projectUser.data.repository.ProjectUserRepository
import com.task.management.workflow.projectUser.presentation.ProjectUserViewModel
import com.task.management.workflow.task.data.remote.TaskService
import com.task.management.workflow.task.data.repository.TaskRepository
import com.task.management.workflow.task.presentation.taskList.TaskListViewModel
import retrofit2.Retrofit

class ViewModelContainer(context: Context) {

    private val retrofit: Retrofit = AppServiceConstants.retrofit
    private val dao = AppServiceConstants.provideDao(context)
    private val tokenProvider = AppServiceConstants.provideTokenProvider()

    // Services
    private val iamService = retrofit.create(IAMService::class.java)
    private val calendarService = retrofit.create(PackageService::class.java)
    private val taskService = retrofit.create(TaskService::class.java)
    private val profileService = retrofit.create(ProfileService::class.java)
    private val projectService = retrofit.create(ProjectService::class.java)
    private val projectUserService = retrofit.create(ProjectUserService::class.java)

    // Repositories
    private val iamRepository = IAMRepository(iamService, dao.getAccountDao())
    private val packageRepository = PackageRepository(calendarService)
    private val taskRepository = TaskRepository(taskService, dao.getTaskDao())
    private val profilesRepository = ProfileRepository(profileService)
    private val projectLocalRepository = LocalProjectRepository(dao.getProjectDao())
    private val projectRemoteRepository = RemoteProjectRepository(projectService)
    private val projectRepository = ProjectRepository(projectLocalRepository, projectRemoteRepository)
    private val projectUserRepository = ProjectUserRepository(projectUserService)

    // ViewModels
    val signInViewModel = SignInViewModel(iamRepository, tokenProvider)
    val signUpViewModel = SignUpViewModel(iamRepository)
    @RequiresApi(Build.VERSION_CODES.O)
    val calendarViewModel = PackageListEventsViewModel(packageRepository, taskRepository)
    val taskViewModel = TaskListViewModel(taskRepository)
    val profilesViewModel = ProfilesViewModel(profilesRepository, signInViewModel)
    val projectViewModel = ProjectViewModel(projectRepository, taskRepository)
    val projectUserViewModel = ProjectUserViewModel(projectUserRepository)

}