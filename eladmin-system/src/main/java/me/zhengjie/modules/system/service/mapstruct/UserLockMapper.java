package me.zhengjie.modules.system.service.mapstruct;

import me.zhengjie.modules.system.domain.UserLock;
import me.zhengjie.modules.system.service.dto.UserLockDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author admin
 * @date 2025-06-27
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserLockMapper {

    UserLockDto toDto(UserLock userLock);
}
