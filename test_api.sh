#!/bin/bash

# 测试API端点
API_URL="https://m1.apifoxmock.com/m1/7056903-6777091-6404548/api/v2/bid"

echo "测试API: $API_URL"
echo ""

# 发送POST请求
response=$(curl -s -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "test-bid-request",
    "version": "2.5",
    "imp": [
      {
        "id": "1",
        "tagid": "test_slot",
        "banner": {
          "w": 320,
          "h": 50
        }
      }
    ],
    "app": {
      "id": "com.maxrtb.zxdm",
      "name": "ZX Demo"
    }
  }')

echo "API Response:"
echo "$response" | jq . 2>/dev/null || echo "$response"

