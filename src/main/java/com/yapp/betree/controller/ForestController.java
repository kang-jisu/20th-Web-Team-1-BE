package com.yapp.betree.controller;

import com.yapp.betree.dto.response.ForestResponseDto;
import com.yapp.betree.dto.response.TreeFullResponseDto;
import com.yapp.betree.service.FolderService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequiredArgsConstructor
@Slf4j
public class ForestController {

    private FolderService folderService;

    /**
     * 유저 나무숲 조회
     * @param userId
     * @return ForestResponseDto
     */
    @GetMapping("/api/forest")
    public ResponseEntity<ForestResponseDto> userForest(
            @RequestParam Long userId) {

        log.info("나무숲 조회 userId: {}", userId);
        return ResponseEntity.ok(folderService.userForest(userId));
    }

    /**
     * 유저 상세 나무 조회
     * @param userId
     * @param treeId
     * @return TreeFullResponseDto
     */
    @GetMapping("/api/forest/{treeId}")
    public ResponseEntity<TreeFullResponseDto> userDetailTree(
            @RequestParam Long userId,
            @PathVariable Long treeId) {

        log.info("유저 상세 나무 조회 userId: {}", userId);
        return ResponseEntity.ok(folderService.userDetailTree(userId, treeId));
    }
}