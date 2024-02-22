package com.mezhendosina.sgo.di

import com.mezhendosina.sgo.app.model.announcements.AnnouncementsSource
import com.mezhendosina.sgo.app.model.grades.GradesSource
import com.mezhendosina.sgo.app.model.journal.DiarySource
import com.mezhendosina.sgo.data.netschool.api.announcements.RetrofitAnnouncementsSource
import com.mezhendosina.sgo.data.netschool.api.attachments.AttachmentsSource
import com.mezhendosina.sgo.data.netschool.api.attachments.RetrofitAttachmentsSource
import com.mezhendosina.sgo.data.netschool.api.diary.RetrofitDiarySource
import com.mezhendosina.sgo.data.netschool.api.grades.RetrofitGradesSource
import com.mezhendosina.sgo.data.netschool.api.homework.HomeworkSource
import com.mezhendosina.sgo.data.netschool.api.homework.RetrofitHomeworkSource
import com.mezhendosina.sgo.data.netschool.api.settings.RetrofitSettingsSource
import com.mezhendosina.sgo.data.netschool.api.settings.SettingsSource
import com.mezhendosina.sgo.data.netschoolEsia.login.LoginSource
import com.mezhendosina.sgo.data.netschoolEsia.login.RetrofitLoginSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SourcesModule {
    @Binds
    abstract fun bindAnnouncementsSource(retrofitAnnouncementsSource: RetrofitAnnouncementsSource): AnnouncementsSource

    @Binds
    abstract fun bindAttachmentsSource(retrofitAttachmentsSource: RetrofitAttachmentsSource): AttachmentsSource

    @Binds
    abstract fun bindDiarySource(retrofitDiarySource: RetrofitDiarySource): DiarySource

    @Binds
    abstract fun bindGradesSource(retrofitGradesSource: RetrofitGradesSource): GradesSource

    @Binds
    abstract fun bindHomeworkSource(retrofitHomeworkSource: RetrofitHomeworkSource): HomeworkSource

    @Binds
    abstract fun bindLoginSource(retrofitLoginSource: RetrofitLoginSource): LoginSource

    @Binds
    abstract fun bindSettingsSource(retrofitSettingsSource: RetrofitSettingsSource): SettingsSource
}
